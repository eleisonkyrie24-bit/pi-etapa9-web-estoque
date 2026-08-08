# Versionamento - PI Etapa 9

Foi criado um repositório Git independente para a Etapa 9, com branch principal `main` e commits semânticos separados por responsabilidade.

Histórico principal:

- `0042c0d` - chore: cria projeto Spring MVC da etapa 9
- `abecc5f` - refactor: reaproveita dominio e servicos das etapas 6 e 7
- `5812863` - feat: implementa persistencia JPA com banco H2
- `96d2c1c` - feat: integra front-end da etapa 8 ao Spring MVC
- `bd4b59f` - test: integra JUnit da etapa 7 e testes web JPA
- `65d429f` - fix: estabiliza configuracao de templates e repositorios JPA
- `892b0f1` - fix: informa versao do modulo Java Time do Thymeleaf

A conexão disponível nesta sessão permitiu validar o código por GitHub Actions e registrar issues, mas não disponibilizou uma operação de criação de um novo repositório remoto. Portanto, o projeto está versionado como novo repositório Git local e pronto para publicação em um repositório GitHub próprio.

Com um repositório vazio chamado `pi-etapa9-web-estoque` criado no GitHub, publicar com:

```bash
git remote add origin https://github.com/eleisonkyrie24-bit/pi-etapa9-web-estoque.git
git push -u origin main
```
