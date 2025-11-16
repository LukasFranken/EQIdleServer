function createShip(name, type) {
	fetch('/shipyard/create', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ name: name, type: type })
	})
	.then(response => response.json())
	.then(data => {
		if (data === 'SUCCESS') {
			toggleModal(false);
			location.reload();
		} else {
			document.getElementById('response-label').textContent = data.replace('_', ' ').toLowerCase();
		}
	})
	.catch(error => {
		document.getElementById('response-label').textContent = 'Error occurred';
		console.error('Error:', error);
	});
}
