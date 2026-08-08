# Plano de testes executado - PI Etapa 9

A execução combina os 28 casos planejados na Etapa 7 com cobertura automatizada adicional de JPA, Spring MVC e um roteiro HTTP ponta a ponta.

| Grupo | Requisitos/casos principais | Evidência final |
|---|---|---|
| Produto | cadastro, SKU normalizado/único, campos obrigatórios, preço válido, consulta/listagem, alteração e remoção condicionada | JUnit + HTTP |
| Estoque | entrada positiva, saída positiva, quantidades inválidas e bloqueio de saldo insuficiente | JUnit + MockMvc + HTTP |
| Histórico | registro de movimentos, listagem geral e filtro por SKU | JUnit/JPA/MockMvc + HTTP |
| Financeiro | valor por produto e valor total do estoque | JUnit + conferência HTTP do estado persistido |
| Persistência | salvar/recuperar produto; persistir movimentação e saldo | Spring Boot + JPA + H2 em memória |
| Web | páginas, formulários, detalhe, movimentações, histórico e navegação | MockMvc + HTTP |
| REST | consulta de produto, 404 e rejeição de saída inválida | MockMvc/JSON + HTTP |

## Resultado JUnit

- `ProdutoTest`: 4 testes.
- `ProdutoServiceTest`: 3 testes.
- `EstoqueServiceTest`: 2 testes.
- `PersistenciaJpaIntegrationTest`: 2 testes.
- `WebMvcIntegrationTest`: 5 testes.
- Total: 16 testes.
- Falhas: 0.
- Erros: 0.
- Ignorados: 0.

## Roteiro HTTP ponta a ponta

O arquivo `scripts/e2e-http.sh` deve ser executado com a aplicação ativa em `localhost:8080`. O workflow `.github/workflows/testes-etapa9.yml` faz isso automaticamente depois da suíte JUnit.

O roteiro valida, entre outros pontos:

- CT01–CT06: cadastro válido, normalização, duplicidade e rejeições de SKU/nome/preço;
- CT07–CT11: consulta, 404, listagem e alteração de preço com preservação da RN03;
- CT12–CT18: entrada, saída, quantidades zero, saldo insuficiente, histórico e filtro por SKU;
- CT19–CT20: cálculo financeiro a partir do estado persistido;
- CT21–CT22: bloqueio de exclusão com saldo e exclusão após zerar o estoque;
- CT23–CT26: formulários e navegação Web;
- CT27–CT28: endpoint existente e operação inválida com HTTP 400, mantendo o saldo.

A validação remota em Java 17 executou a suíte JUnit e o roteiro HTTP, encerrando ambos com sucesso.
