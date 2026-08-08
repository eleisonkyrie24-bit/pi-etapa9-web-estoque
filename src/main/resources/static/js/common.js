document.querySelectorAll('[data-uppercase]').forEach((campo) => {
    campo.addEventListener('input', () => campo.value = campo.value.toUpperCase());
});
document.querySelectorAll('form[data-validate]').forEach((form) => {
    form.addEventListener('submit', (event) => {
        if (!form.checkValidity()) {
            event.preventDefault();
            form.reportValidity();
        }
    });
});
