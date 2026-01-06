function initializeComponentLevels() {
	document.querySelectorAll('.component-level-row').forEach(row => {
		row.addEventListener('click', function() {
			switchToLevelAttributes(this);
		});
	});

	document.querySelectorAll('.component-level-row .edit-btn').forEach(btn => {
		btn.addEventListener('click', function(e) {
			e.stopPropagation();
			const row = this.closest('.component-level-row');
			triggerComponentLevelEditMode(row);
		});
	});
}

function switchToLevelAttributes(row) {
	if (row.classList.contains('edit-mode')) return;
	const shipname = row.getAttribute('data-shipname');
	const componentId = row.getAttribute('data-component-id');
	const level = row.getAttribute('data-level');

	updateLevelAttributes(shipname, componentId, level);
	
	document.getElementById('componentlevel').textContent = level;
}

function backToComponents() {
	const componentsView = document.querySelector('.components-view');
	const levelView = document.querySelector('.component-level-view');

	componentsView.classList.remove('slide-out');
	levelView.classList.remove('slide-in');

	document.getElementById('componentname').textContent = '';
	document.getElementById('componentid').textContent = '';
	document.getElementById('componenttype').textContent = '';
}

function addLevel(shipname, componentid) {
	const request = {
		shipname: shipname,
		componentID: componentid
	};

	fetch('/shipyard/create/componentlevel', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(request)
	})
		.then(response => response.json())
		.then(data => {
			if (data === 'SUCCESS') {
				updateComponentLevels(shipname, componentid);
			}
		})
		.catch(error => {
			console.error('Error:', error);
		});
}

function triggerComponentLevelEditMode(row) {
	const componentType = row.getAttribute('data-component-name');
	fetch(`/shipyard/component-level-types/${componentType}`)
		.then(response => response.json())
		.then(options => {
			updateComponentLevelEditSection(row, options);
		});
}

function updateComponentLevelEditSection(row, options) {
	const componentId = document.getElementById('componentid').textContent;
	const level = row.getAttribute('data-level');
	const leveltype = row.querySelector('td:nth-child(2)').textContent;
	const value = row.querySelector('td:nth-child(3)').textContent;
	const shipname = document.getElementById('shipname').textContent
	row.classList.add('edit-mode');
	createSelectElement(row, options, leveltype, 2);
	createValueInputElement(row, value, 3);
	row.querySelector('.component-level-actions').innerHTML = `
											<button class="cancel-btn">✕</button>
											<button class="confirm-btn">✓</button>
											<button class="delete-btn">🗑️</button>
										`;

	// Cancel handler
	row.querySelector('.cancel-btn').addEventListener('click', function(e) {
		e.stopPropagation();
		revertComponentLevelEditMode(row, leveltype, value);
	});

	// Confirm handler
	row.querySelector('.confirm-btn').addEventListener('click', function(e) {
		e.stopPropagation();
		const newLeveltype = row.querySelector('.edit-type').value;
		const newValue = row.querySelector('.edit-value').value;
		fetch('/shipyard/update/componentlevel', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ shipname, componentId, level, requirementType: newLeveltype, requirementValue: newValue })
		}).then(() => {
			revertComponentLevelEditMode(row, newLeveltype, newValue);
			updateComponentLevels(shipname, componentId);
		});
	});

	// Delete handler
	const rowDeleteBtn = row.querySelector('.delete-btn');
	if (rowDeleteBtn !== null) {
		rowDeleteBtn.addEventListener('click', function(e) {
			e.stopPropagation();
			if (confirm('Delete level ' + level + '?')) {
				fetch('/shipyard/delete/componentlevel', {
					method: 'POST',
					headers: { 'Content-Type': 'application/json' },
					body: JSON.stringify({ shipname, componentId, level })
				}).then(() => {
					updateComponentLevels(shipname, componentId);
				});
			}
		});
	}
}

function revertComponentLevelEditMode(row, leveltype, value) {
	row.classList.remove('edit-mode');
	row.querySelector('td:nth-child(2)').textContent = leveltype;
	row.querySelector('td:nth-child(3)').textContent = value;
	row.querySelector('.component-level-actions').innerHTML = '<button class="edit-btn">Edit</button>';
	row.querySelector('.edit-btn').addEventListener('click', function(e) {
		e.stopPropagation();
		triggerComponentLevelEditMode(row);
	});
}

function updateComponentLevels(shipname, componentId) {
	const endpoint = `/shipyard/modal/shipoverviewmodal/fragment/${shipname}/${componentId}`;
	fetch(endpoint, { method: 'GET' })
		.then(response => response.text())
		.then(html => {
			const viewContent = document.querySelector('.component-level-view');
			const tempDiv = document.createElement('div');
			tempDiv.innerHTML = html;
			const newElement = tempDiv.firstElementChild;

			const parent = viewContent.parentNode;
			parent.replaceChild(newElement, viewContent);

			initializeComponentLevels();
			slideToComponentLevels();
		})
		.catch(error => console.error('Error loading modal:', error));
}

function slideToComponentLevels() {
	const container = document.querySelector('.modal-view-container');
	const componentsView = container.querySelector('.components-view');
	const componentLevelsView = document.querySelector('.component-level-view');
	componentsView.classList.add('slide-out');
	requestAnimationFrame(() => {
		componentLevelsView.classList.add('slide-in');
	});
}