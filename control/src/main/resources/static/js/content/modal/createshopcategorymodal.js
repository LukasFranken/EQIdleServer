function createShopCategory(name) {
	fetch('/shop/create/category', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ name })
	})
	.then(response => response.json())
	.then(data => {
		if (data === 'SUCCESS') {
			toggleModal(false);
			location.reload();
		} else {
			document.getElementById('response-label').textContent = "Error: " + data.replaceAll('_', ' ').toLowerCase();
		}
	})
	.catch(error => {
		document.getElementById('response-label').textContent = 'Error occurred';
		console.error('Error:', error);
	});
}
