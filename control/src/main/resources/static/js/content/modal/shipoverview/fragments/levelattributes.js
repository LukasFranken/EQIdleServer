function initializeLevelAttributes() {
	document.querySelectorAll('.attribute-row .edit-btn').forEach(btn => {
		btn.addEventListener('click', function(e) {
			e.stopPropagation();
			const row = this.closest('.attribute-row');
			triggerLevelAttributeEditMode(row);
		});
	});
}

function addAttribute(shipname, componentid, componentlevel) {
	const request = {
		shipname: shipname,
		componentId: componentid,
		level: componentlevel
	};

	fetch('/shipyard/create/levelattribute', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(request)
	})
		.then(response => response.json())
		.then(data => {
			if (data === 'SUCCESS') {
				updateLevelAttributes(shipname, componentid, componentlevel);
			}
		})
		.catch(error => {
			console.error('Error:', error);
		});
}

function triggerLevelAttributeEditMode(row) {
	const componentType = row.getAttribute('data-component-name');
	fetch(`/shipyard/level-attribute-types/${componentType}`)
		.then(response => response.json())
		.then(options => {
			updateLevelAttributeEditSection(row, options);
		});
}

function updateLevelAttributeEditSection(row, options) {
	const componentId = document.getElementById('componentid').textContent;
	const level = document.getElementById('componentlevel').textContent
	const attributeId = row.querySelector('td:nth-child(1)').textContent;
	const type = row.querySelector('td:nth-child(2)').textContent;
	const value = row.querySelector('td:nth-child(3)').textContent;
	const shipname = document.getElementById('shipname').textContent
	row.classList.add('edit-mode');
	createSelectElement(row, options, type, 2);
	createValueInputElement(row, value, 3);
	row.querySelector('.level-attribute-actions').innerHTML = `
											<button class="cancel-btn">✕</button>
											<button class="confirm-btn">✓</button>
											<button class="delete-btn">🗑️</button>
										`;

	// Cancel handler
	row.querySelector('.cancel-btn').addEventListener('click', function(e) {
		e.stopPropagation();
		revertLevelAttributeEditMode(row, type, value);
	});

	// Confirm handler
	row.querySelector('.confirm-btn').addEventListener('click', function(e) {
		e.stopPropagation();
		const newType = row.querySelector('.edit-type').value;
		const newValue = row.querySelector('.edit-value').value;
		fetch('/shipyard/update/levelattribute', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ shipname, componentId, level, attributeId, type: newType, value: newValue })
		}).then(() => {
			revertLevelAttributeEditMode(row, newType, newValue);
			updateLevelAttributes(shipname, componentId, level);
		});
	});

	// Delete handler
	const rowDeleteBtn = row.querySelector('.delete-btn');
	if (rowDeleteBtn !== null) {
		rowDeleteBtn.addEventListener('click', function(e) {
			e.stopPropagation();
			if (confirm('Delete attribute with ID ' + attributeId + '?')) {
				fetch('/shipyard/delete/levelattribute', {
					method: 'POST',
					headers: { 'Content-Type': 'application/json' },
					body: JSON.stringify({ shipname, componentId, level, attributeId })
				}).then(() => {
					updateLevelAttributes(shipname, componentId, level);
				});
			}
		});
	}
}

function revertLevelAttributeEditMode(row, type, value) {
	row.classList.remove('edit-mode');
	row.querySelector('td:nth-child(2)').textContent = type;
	row.querySelector('td:nth-child(3)').textContent = value;
	row.querySelector('.level-attribute-actions').innerHTML = '<button class="edit-btn">Edit</button>';
	row.querySelector('.edit-btn').addEventListener('click', function(e) {
		e.stopPropagation();
		triggerLevelAttributeEditMode(row);
	});
}

function updateLevelAttributes(shipname, componentId, level) {
	const endpoint = `/shipyard/modal/shipoverviewmodal/fragment/${shipname}/${componentId}/${level}`;
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
		.catch(error => console.error('Error loading modal:', error));
}

function slideToLevelAttributes() {
	const container = document.querySelector('.modal-view-container');
	const fromView = container.querySelector('.component-level-view');
	const toView = document.querySelector('.level-attribute-view');
	fromView.classList.add('slide-out');
	requestAnimationFrame(() => {
		toView.classList.add('slide-in');
	});
}

function backToLevels() {
	const levelView = document.querySelector('.component-level-view');
	const levelAttributeView = document.querySelector('.level-attribute-view');

	levelView.classList.remove('slide-out');
	levelAttributeView.classList.remove('slide-in');
	document.getElementById('componentlevel').textContent = '';
}