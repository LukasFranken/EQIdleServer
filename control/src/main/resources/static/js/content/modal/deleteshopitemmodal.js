function deleteShopItem(itemId) {
	fetch('/shop/delete/item/' + itemId, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' }
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
