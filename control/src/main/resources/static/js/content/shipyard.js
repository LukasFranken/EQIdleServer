function refresh() {
	fetch('/shipyard/refresh', { method: 'GET' })
	            .then(response => {
	                if (response.ok) {
	                    location.reload();
	                } else {
	                    console.error('Failed to refresh');
	                }
	            })
	            .catch(error => console.error('Error:', error));
}