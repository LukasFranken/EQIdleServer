function initializeBuildCostModal() {
	
}

function addBuildCost(shipname) {
	const request = {
			shipname: shipname
		};

		fetch('/shipyard/create/buildcost', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(request)
		})
			.then(response => response.json())
			.then(data => {
				if (data === 'SUCCESS') {
					updateBuildCosts(shipname);
				}
			})
			.catch(error => {
				console.error('Error:', error);
			});
}

function updateBuildCosts(shipname) {
	/*const endpoint = `/shipyard/modal/buildcostmodal/${shipname}`;
	fetch(endpoint, { method: 'GET' })
		.then(response => response.text())
		.then(html => {
			const viewContent = document.querySelector('.level-attribute-view');
			const tempDiv = document.createElement('div');
			tempDiv.innerHTML = html;
			const newElement = tempDiv.firstElementChild;

			const parent = viewContent.parentNode;
			parent.replaceChild(newElement, viewContent);

			initializeLevelAttributes();
			slideToLevelAttributes();
		})
		.catch(error => console.error('Error loading modal:', error));*/
}