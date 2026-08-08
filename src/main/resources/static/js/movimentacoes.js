const tipo = document.querySelector('#tipo-movimentacao');
if (tipo) tipo.addEventListener('change', () => document.body.dataset.movimentacao = tipo.value.toLowerCase());
