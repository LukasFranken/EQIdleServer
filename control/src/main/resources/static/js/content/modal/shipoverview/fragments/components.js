function initializeComponents() {
	document.querySelectorAll('.component-row').forEach(row => {
		row.addEventListener('click', function() {
			switchToComponentLevels(this);
		});
	});

	document.querySelectorAll('.component-row .edit-btn').forEach(btn => {
		btn.addEventListener('click', function(e) {
			e.stopPropagation();
			const row = this.closest('.component-row');
			triggerComponentEditMode(row);
		});
	});

	initializeComponentTypeDropdown();
}

function initializeComponentTypeDropdown() {
	const typeSelect = document.getElementById('type');
	typeSelect.addEventListener('change', function() {
		updateComponentTypes(this.value);
	});

	updateComponentTypes(typeSelect.value);
}

function switchToComponentLevels(row) {
	if (row.classList.contains('edit-mode')) return;
	const componentName = row.getAttribute('data-component-name');
	const shipname = row.getAttribute('data-shipname');
	const componentId = row.getAttribute('data-component-id');
	const componentType = row.getAttribute('data-component-type');

	updateComponentLevels(shipname, componentId);

	document.getElementById('componentname').textContent = componentName;
	document.getElementById('componentid').textContent = componentId;
	document.getElementById('componenttype').textContent = componentType;
}

function updateComponentTypes(selectedType) {
	fetch(`/shipyard/component-types/${selectedType}`)
		.then(response => response.json())
		.then(data => {
			const componentTypeSelect = document.getElementById('component-type');
			componentTypeSelect.innerHTML = '';
			data.forEach(option => {
				const opt = document.createElement('option');
				opt.value = option;
				opt.textContent = option;
				componentTypeSelect.appendChild(opt);
			});
		})
		.catch(error => console.error('Error fetching component types:', error));
}

function createComponent(shipname, type, componentType) {
	const request = {
		shipname: shipname,
		type: type,
		componentType: componentType
	};

	fetch('/shipyard/create/component', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(request)
	})
		.then(response => response.json())
		.then(data => {
			if (data === 'SUCCESS') {
				document.getElementById('create-component-response-label').textContent = 'Component created successfully!';
				document.getElementById('create-component-response-label').style.color = 'green';
				updateComponents(shipname);
			} else {
				document.getElementById('create-component-response-label').textContent = 'Error: ' + data;
				document.getElementById('create-component-response-label').style.color = 'red';
			}
		})
		.catch(error => {
			console.error('Error:', error);
			document.getElementById('create-component-response-label').textContent = 'Failed to create component.';
		});

}

function triggerComponentEditMode(row) {
	const componentType = row.getAttribute('data-component-name');
	fetch(`/shipyard/component-types/${componentType}`)
		.then(response => response.json())
		.then(options => {
			updateComponentEditSection(row, options);
		});
}

function updateComponentEditSection(row, options) {
	const componentType = row.getAttribute('data-component-name');
	const componentSubtype = row.querySelector('td:nth-child(3)').textContent;
	const shipname = row.getAttribute('data-shipname');
	const id = row.getAttribute('data-component-id');
	row.classList.add('edit-mode');
	createSelectElement(row, options, componentSubtype, 3);
	if (componentType === 'CORE') {
		row.querySelector('.component-actions').innerHTML = `
					                <button class="cancel-btn">✕</button>
					                <button class="confirm-btn">✓</button>
					            `;
	} else {
		row.querySelector('.component-actions').innerHTML = `
									<button class="cancel-btn">✕</button>
									<button class="confirm-btn">✓</button>
									<button class="delete-btn">🗑️</button>
								`;
	}

	// Cancel handler
	row.querySelector('.cancel-btn').addEventListener('click', function(e) {
		e.stopPropagation();
		revertComponentEditMode(row, componentSubtype);
	});

	// Confirm handler
	row.querySelector('.confirm-btn').addEventListener('click', function(e) {
		e.stopPropagation();
		const newSubtype = row.querySelector('.edit-type').value;
		fetch('/shipyard/update/component', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ shipname, id, type: newSubtype })
		}).then(() => {
			revertComponentEditMode(row, newSubtype);
			updateComponents(shipname);
		});
	});

	// Delete handler
	const rowDeleteBtn = row.querySelector('.delete-btn');
	if (rowDeleteBtn !== null) {
		rowDeleteBtn.addEventListener('click', function(e) {
			e.stopPropagation();
			if (confirm('Delete component ' + componentType + ' with ID ' + id + '?')) {
				fetch('/shipyard/delete/component', {
					method: 'POST',
					headers: { 'Content-Type': 'application/json' },
					body: JSON.stringify({ shipname, id })
				}).then(() => {
					updateComponents(shipname);
				});
			}
		});
	}
}

function revertComponentEditMode(row, subtype) {
	row.classList.remove('edit-mode');
	row.querySelector('td:nth-child(3)').textContent = subtype;
	row.querySelector('.component-actions').innerHTML = '<button class="edit-btn">Edit</button>';
	row.querySelector('.edit-btn').addEventListener('click', function(e) {
		e.stopPropagation();
		triggerComponentEditMode(row);
	});
}

function updateComponents(shipname) {
	const endpoint = `/shipyard/modal/shipoverviewmodal/fragment/${shipname}`;
	fetch(endpoint, { method: 'GET' })
		.then(response => response.text())
		.then(html => {
			const viewContent = document.querySelector('.components-view');
			const tempDiv = document.createElement('div');
			tempDiv.innerHTML = html;
			const newElement = tempDiv.firstElementChild;

			const parent = viewContent.parentNode;
			parent.replaceChild(newElement, viewContent);
			initializeBlueprintModal();
		})
		.catch(error => console.error('Error loading modal:', error));
}