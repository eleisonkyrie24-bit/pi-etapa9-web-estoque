const filtro = document.querySelector('#filtro-sku');
if (filtro) filtro.addEventListener('keydown', (e) => { if (e.key === 'Escape') filtro.value = ''; });
