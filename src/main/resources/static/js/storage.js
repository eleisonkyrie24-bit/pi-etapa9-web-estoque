/*
 * Compatibilidade estrutural com a Etapa 8.
 * Na Etapa 9 o navegador NÃO é mais a fonte de persistência: os dados são gravados
 * pelo back-end Spring em banco H2 via JPA. Este arquivo permanece para deixar
 * explícita a migração e evitar que regras de negócio voltem ao localStorage.
 */
window.EstoqueStorage = Object.freeze({ backend: 'JPA/H2', localStorageAtivo: false });
