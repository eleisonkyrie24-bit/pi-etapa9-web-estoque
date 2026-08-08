# Relatório técnico - PI Etapa 9

## Objetivo

Concluir o Sistema Web de Controle de Estoque, reutilizando o domínio/refatoração da Etapa 6, os testes e plano da Etapa 7 e as páginas da Etapa 8.

## Arquitetura

- `model`: entidades e regras locais (`Produto`, `MovimentacaoEstoque`, `TipoMovimentacao`).
- `service`: regras de aplicação (`ProdutoService`, `EstoqueService`).
- `repository`: contratos herdados da Etapa 6.
- `repository.jpa`: adaptadores e interfaces Spring Data JPA.
- `repository.memory`: implementações em memória mantidas para testes unitários rápidos.
- `web`: controllers Spring MVC e REST.
- `templates` e `static`: front-end integrado da Etapa 8.

A persistência foi migrada para JPA/H2 sem acoplar os serviços diretamente ao Spring Data. Os serviços continuam dependendo das interfaces de repositório, preservando a separação de responsabilidades estabelecida na Etapa 6.

## Integração das etapas

- Etapa 6: domínio, serviços, regras de negócio e interfaces de repositório reaproveitados.
- Etapa 7: nove testes JUnit preservados/adaptados e ampliados com testes de persistência e Web MVC.
- Etapa 8: cinco páginas, três folhas CSS e scripts JavaScript mantidos na mesma divisão funcional, agora alimentados pelo back-end.
- Etapa 9: Spring MVC, Thymeleaf, JPA/Hibernate, H2 persistente, API REST e testes de integração.

## Banco de dados

O ambiente de execução usa `jdbc:h2:file:./data/estoque` e `spring.jpa.hibernate.ddl-auto=update`. O ambiente de teste usa banco H2 em memória com schema descartável.

## Bugtracking

- BUG-001: filtro vazio de histórico retornava lista vazia; corrigido no serviço e protegido por teste de regressão.
- BUG-002: dependência `thymeleaf-extras-java8time` sem versão bloqueava o build Maven; corrigido com versão explícita `3.0.4.RELEASE` e revalidado por CI.

## Validação final

GitHub Actions, Java Temurin 17.0.19, Maven: 16 testes executados, 0 falhas, 0 erros, 0 ignorados; build concluído com sucesso.
