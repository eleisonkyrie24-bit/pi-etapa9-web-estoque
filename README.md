# PI Etapa 9 - Sistema Web de Controle de Estoque

Aplicação Java Web que conclui a evolução iniciada nas Etapas 6, 7 e 8 do Projeto Integrador.

## Tecnologias

- Java 17
- Apache Maven
- Spring Boot 3.5.16
- Spring MVC + Thymeleaf
- Spring Data JPA / Hibernate
- H2 em arquivo
- JUnit 5 + Spring Boot Test + MockMvc
- Git / GitHub Actions

## Funcionalidades

- cadastro de produtos por SKU, nome e preço;
- consulta e listagem de produtos;
- alteração de preço;
- remoção de produto somente com saldo zero;
- entrada e saída de estoque com validações de quantidade e saldo;
- histórico geral e por SKU;
- cálculo do valor financeiro total do estoque;
- dashboard web;
- API REST auxiliar para consulta e movimentação.

## Páginas integradas da Etapa 8

- `/` - dashboard;
- `/produtos` - cadastro e listagem;
- `/movimentacoes` - entrada e saída;
- `/historico` - histórico e filtro por SKU;
- `/produto/{sku}` - detalhe do produto.

## API

- `GET /api/produtos`
- `GET /api/produtos/{sku}`
- `POST /api/movimentacoes`

Exemplo de JSON para movimentação:

```json
{"sku":"TEC-001","tipo":"ENTRADA","quantidade":10}
```

## Banco de dados

A aplicação usa JPA/Hibernate com H2 persistente em `./data/estoque`. Em testes, usa H2 em memória e recria o schema a cada execução.

## Execução

```bash
mvn clean test
mvn spring-boot:run
```

Acesse `http://localhost:8080`.

No NetBeans: **File > Open Project**, selecione esta pasta Maven e execute **Run Project**. O arquivo `nbactions.xml` já contém as ações de execução e testes.

## Testes

A suíte final possui 16 testes: 9 herdados/adaptados da Etapa 7 e 7 testes de integração da Etapa 9 (JPA e Web MVC). A validação remota em Java 17 concluiu com `Tests run: 16, Failures: 0, Errors: 0, Skipped: 0` e `BUILD SUCCESS`.

Consulte `docs/RELATORIO_TECNICO_ETAPA9.md` e `docs/PLANO_TESTES_EXECUTADO_ETAPA9.md`.
