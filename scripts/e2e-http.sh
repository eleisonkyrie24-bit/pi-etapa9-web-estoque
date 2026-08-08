#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"

status_post() {
  local url="$1"; shift
  curl -sS -o /dev/null -w '%{http_code}' -X POST "$BASE_URL$url" "$@"
}

json_product() {
  curl -fsS "$BASE_URL/api/produtos/$1"
}

assert_json_product() {
  local sku="$1" expected_name="$2" expected_price="$3" expected_qty="$4"
  json_product "$sku" | python -c '
import json, sys
p=json.load(sys.stdin)
name, price, qty=sys.argv[1], sys.argv[2], int(sys.argv[3])
assert p["nome"] == name, p
assert abs(float(p["preco"]) - float(price)) < 1e-9, p
assert int(p["quantidadeEmEstoque"]) == qty, p
' "$expected_name" "$expected_price" "$expected_qty"
}

assert_product_absent() {
  local marker="$1"
  if curl -fsS "$BASE_URL/api/produtos" | grep -Fq "$marker"; then
    echo "Falha: produto inválido persistiu: $marker" >&2
    exit 1
  fi
}

# CT26 - navegação pelas páginas principais
for route in / /produtos /movimentacoes /historico; do
  curl -fsS "$BASE_URL$route" >/dev/null
done

# CT03/CT04/CT05/CT06/CT24 - validações de cadastro
[[ "$(status_post /produtos -d 'sku=   ' -d 'nome=INVALIDO-SKU' -d 'preco=10.00')" == "302" ]]
assert_product_absent 'INVALIDO-SKU'
[[ "$(status_post /produtos -d 'sku=INV-NOME' -d 'nome=   ' -d 'preco=10.00')" == "302" ]]
assert_product_absent 'INV-NOME'
[[ "$(status_post /produtos -d 'sku=INV-ZERO' -d 'nome=Preco Zero' -d 'preco=0')" == "302" ]]
assert_product_absent 'INV-ZERO'
[[ "$(status_post /produtos -d 'sku=INV-NEG' -d 'nome=Preco Negativo' -d 'preco=-1')" == "302" ]]
assert_product_absent 'INV-NEG'

# CT01/CT23 - cadastro válido e normalização do SKU
[[ "$(status_post /produtos -d 'sku=e2e-001' -d 'nome=Produto E2E' -d 'preco=10.50')" == "302" ]]
assert_json_product 'E2E-001' 'Produto E2E' '10.50' 0
curl -fsS "$BASE_URL/produto/E2E-001" | grep -q 'E2E-001'

# CT02 - duplicidade não cria segundo registro
[[ "$(status_post /produtos -d 'sku=e2e-001' -d 'nome=Duplicado' -d 'preco=11.00')" == "302" ]]
assert_product_absent 'Duplicado'

# CT07/CT08/CT09/CT27 - consultas e listagem
curl -fsS "$BASE_URL/api/produtos/E2E-001" | grep -q 'Produto E2E'
[[ "$(curl -sS -o /dev/null -w '%{http_code}' "$BASE_URL/api/produtos/XXX-999")" == "404" ]]
curl -fsS "$BASE_URL/produtos" | grep -q 'E2E-001'

# CT10/CT11 - alteração de preço válida e inválida
[[ "$(status_post /produto/E2E-001/preco -d 'preco=12.00')" == "302" ]]
assert_json_product 'E2E-001' 'Produto E2E' '12.00' 0
[[ "$(status_post /produto/E2E-001/preco -d 'preco=0')" == "302" ]]
assert_json_product 'E2E-001' 'Produto E2E' '12.00' 0

# CT12/CT13 - entrada válida e quantidade zero rejeitada
[[ "$(status_post /movimentacoes -d 'sku=E2E-001' -d 'tipo=ENTRADA' -d 'quantidade=10')" == "302" ]]
assert_json_product 'E2E-001' 'Produto E2E' '12.00' 10
[[ "$(status_post /movimentacoes -d 'sku=E2E-001' -d 'tipo=ENTRADA' -d 'quantidade=0')" == "302" ]]
assert_json_product 'E2E-001' 'Produto E2E' '12.00' 10

# CT14/CT15 - saída válida e quantidade zero rejeitada
[[ "$(status_post /movimentacoes -d 'sku=E2E-001' -d 'tipo=SAIDA' -d 'quantidade=3')" == "302" ]]
assert_json_product 'E2E-001' 'Produto E2E' '12.00' 7
[[ "$(status_post /movimentacoes -d 'sku=E2E-001' -d 'tipo=SAIDA' -d 'quantidade=0')" == "302" ]]
assert_json_product 'E2E-001' 'Produto E2E' '12.00' 7

# CT16/CT28 - saída acima do saldo retorna erro e não altera saldo
code=$(curl -sS -o /tmp/etapa9-e2e-erro.json -w '%{http_code}' -X POST "$BASE_URL/api/movimentacoes" \
  -H 'Content-Type: application/json' -d '{"sku":"E2E-001","tipo":"SAIDA","quantidade":100}')
[[ "$code" == "400" ]]
grep -qi 'Estoque insuficiente' /tmp/etapa9-e2e-erro.json
assert_json_product 'E2E-001' 'Produto E2E' '12.00' 7

# CT17 - histórico contém entrada e saída
history=$(curl -fsS "$BASE_URL/historico?sku=E2E-001")
grep -q 'ENTRADA' <<<"$history"
grep -q 'SAIDA' <<<"$history"

# CT18 - filtro por SKU não mistura movimentações de outro produto
[[ "$(status_post /produtos -d 'sku=E2E-002' -d 'nome=Produto Auxiliar' -d 'preco=100.00')" == "302" ]]
[[ "$(status_post /movimentacoes -d 'sku=E2E-002' -d 'tipo=ENTRADA' -d 'quantidade=2')" == "302" ]]
filtered=$(curl -fsS "$BASE_URL/historico?sku=E2E-001")
grep -q 'E2E-001' <<<"$filtered"
if grep -q 'E2E-002' <<<"$filtered"; then
  echo 'Falha: filtro de histórico misturou E2E-002' >&2
  exit 1
fi

# CT19/CT20 - valores financeiros conferidos a partir do estado persistido
python - "$BASE_URL" <<'PY'
import json, sys, urllib.request
base=sys.argv[1]
with urllib.request.urlopen(base + '/api/produtos') as r:
    products=json.load(r)
by_sku={p['sku']: p for p in products}
p1=by_sku['E2E-001']; p2=by_sku['E2E-002']
assert abs(float(p1['preco']) * int(p1['quantidadeEmEstoque']) - 84.0) < 1e-9
assert abs(sum(float(p['preco']) * int(p['quantidadeEmEstoque']) for p in products) - 284.0) < 1e-9
PY
curl -fsS "$BASE_URL/" >/dev/null

# CT21 - exclusão bloqueada enquanto houver saldo
headers=$(mktemp)
curl -sS -o /dev/null -D "$headers" -X POST "$BASE_URL/produto/E2E-001/excluir"
grep -qi '^Location:.*\/produto\/E2E-001' "$headers"
assert_json_product 'E2E-001' 'Produto E2E' '12.00' 7
rm -f "$headers"

# CT22 - zerar saldo e excluir
[[ "$(status_post /movimentacoes -d 'sku=E2E-001' -d 'tipo=SAIDA' -d 'quantidade=7')" == "302" ]]
assert_json_product 'E2E-001' 'Produto E2E' '12.00' 0
[[ "$(status_post /produto/E2E-001/excluir)" == "302" ]]
[[ "$(curl -sS -o /dev/null -w '%{http_code}' "$BASE_URL/api/produtos/E2E-001")" == "404" ]]

# CT25 já exercitado por entrada/saída pela interface; CT26 pelas rotas; CT27/28 pela API.
echo 'E2E HTTP: CT01-CT28 cobertos em conjunto com JUnit/JPA/MVC; fluxo ponta a ponta aprovado.'
