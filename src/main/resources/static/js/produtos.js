const busca = document.querySelector('#busca-produto');
if (busca) busca.addEventListener('keydown', (e) => { if (e.key === 'Escape') busca.value = ''; });
