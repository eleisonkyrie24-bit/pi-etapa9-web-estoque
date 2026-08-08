# Plano de testes executado - PI Etapa 9

A execução combina os casos planejados na Etapa 7 com cobertura automatizada adicional de JPA e Web MVC.

| Grupo | Requisitos/casos principais | Evidência final |
|---|---|---|
| Produto | cadastro, SKU normalizado/único, preço válido, consulta/listagem, alteração e remoção condicionada | testes unitários + controllers MVC |
| Estoque | entrada positiva, saída positiva, bloqueio de saldo insuficiente, saldo atualizado | JUnit + MockMvc |
| Histórico | registro de movimentos, listagem geral e filtro por SKU | JUnit/JPA/MockMvc |
| Financeiro | valor por produto e valor total do estoque | JUnit |
| Persistência | salvar e recuperar produto; persistir movimentação e saldo | Spring Boot + JPA + H2 em memória |
| Web | renderização de produto, submissão de entrada, histórico com filtro vazio | MockMvc |
| REST | consulta de produto e rejeição de saída inválida | MockMvc/JSON |

## Resultado automatizado

- `ProdutoTest`: 4 testes.
- `ProdutoServiceTest`: 3 testes.
- `EstoqueServiceTest`: 2 testes.
- `PersistenciaJpaIntegrationTest`: 2 testes.
- `WebMvcIntegrationTest`: 5 testes.
- Total: 16 testes.
- Falhas: 0.
- Erros: 0.
- Ignorados: 0.

## Cobertura dos casos Web previstos na Etapa 7

- CT25: registrar entrada/saída via Web - coberto por formulário MVC e regra de API; entrada validada por MockMvc e saída inválida validada pela API.
- CT26: navegação entre páginas - estrutura de navegação preservada nos cinco templates da Etapa 8; validação final recomendada também em navegador local no momento da apresentação.
- CT27: API deve retornar produto existente - automatizado.
- CT28: operação inválida deve retornar erro esperado - automatizado com HTTP 400 e mensagem de estoque insuficiente.
