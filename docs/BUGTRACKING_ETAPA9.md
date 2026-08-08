# Bugtracking - PI Etapa 9

Ferramenta utilizada: GitHub Issues.

## BUG-001

**Título:** filtro vazio no histórico deve listar todas as movimentações.

Problema: um SKU vazio recebido pelo filtro era interpretado como SKU válido e resultava em histórico vazio. Correção: `EstoqueService.listarHistoricoPorSku` passou a tratar `null`/blank como solicitação de histórico completo. Foi adicionado o teste `deveListarHistoricoQuandoFiltroSkuVazio`.

Registro: issue #2 do repositório de validação, encerrada como `completed` após CI aprovada.

## BUG-002

**Título:** dependência Java Time do Thymeleaf sem versão impede build Maven.

Problema: a primeira execução remota não conseguiu ler o POM. Correção: versão `3.0.4.RELEASE` declarada explicitamente. Commit local: `892b0f1`. A segunda execução remota concluiu 16 testes com sucesso.

Registro: issue #3 do repositório de validação, encerrada como `completed` após CI aprovada.
