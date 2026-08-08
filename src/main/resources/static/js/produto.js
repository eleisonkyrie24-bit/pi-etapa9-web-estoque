document.querySelectorAll('[data-confirm-delete]').forEach((form) => {
    form.addEventListener('submit', (event) => {
        if (!window.confirm('Excluir este produto? A operação só será aceita se o saldo estiver zerado.')) event.preventDefault();
    });
});
