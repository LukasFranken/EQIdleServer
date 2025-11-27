function initializeComponentRows() {
    const container = document.querySelector('.modal-view-container');
    if (!container) return;

    const componentsView = container.querySelector('.components-view');
    const overviewView = container.querySelector('.component-overview-view');
    const levelDetailsView = container.querySelector('.level-details-view');
    const componentTitle = overviewView.querySelector('.component-title');
    const levelTitle = levelDetailsView.querySelector('.level-title');
    const backBtns = container.querySelectorAll('.back-btn');

	// Component row click handler
	document.querySelectorAll('.component-row').forEach(row => {
	    row.addEventListener('click', function () {
	        if (this.classList.contains('edit-mode')) return;
	        const componentName = this.getAttribute('data-component-name');
	        const shipname = this.getAttribute('data-shipname');
	        const componentId = this.getAttribute('data-component-id');
	        const endpoint = `/shipyard/modal/shipoverviewmodal/${shipname}/${componentId}`;
	        
	        fetch(endpoint, { method: 'GET' })
	            .then(response => response.text())
	            .then(html => {
	                const tableContent = document.querySelector('.component-level-table');
	                tableContent.innerHTML = html;
	                if (typeof initializeComponentRows === 'function') {
	                    initializeComponentRows();
	                }
	            })
	            .catch(error => console.error('Error loading modal:', error));
	            
	        componentTitle.textContent = componentName;
	        componentsView.classList.add('slide-out');
	        overviewView.classList.add('slide-in');
	    });
	});

    // Level row click handler
    document.querySelectorAll('.level-row').forEach(row => {
        row.addEventListener('click', function () {
			const shipname = this.getAttribute('data-shipname');
			const componentId = this.getAttribute('data-component-id');
            const level = this.getAttribute('data-level');
			
			const endpoint = `/shipyard/modal/shipoverviewmodal/${shipname}/${componentId}/${level}`;
						
						fetch(endpoint, { method: 'GET' })
						            .then(response => response.text())
						            .then(html => {
										const tableContent = document.querySelector('.level-attribute-table');
										tableContent.innerHTML = html;
										if (typeof initializeComponentRows === 'function') {
											initializeComponentRows();
										}
						            })
						            .catch(error => console.error('Error loading modal:', error));
			
            levelTitle.textContent = `Level ${level}`;
            overviewView.classList.add('slide-out');
            levelDetailsView.classList.add('slide-in');
        });
    });
	
	// Component row edit button handler
		document.querySelectorAll('.component-row .edit-btn').forEach(btn => {
		    btn.addEventListener('click', function (e) {
		        e.stopPropagation();
		        const row = this.closest('.component-row');
		        triggerComponentEditMode(row);
		    });
		});

    // Attribute row edit button handler
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', function (e) {
			e.stopPropagation();
            const row = this.closest('.attribute-row');
			triggerEditMode(row);
        });
    });
	
	// Populate component-type on type change
	const typeSelect = document.getElementById('type');
	const componentTypeSelect = document.getElementById('component-type');

	function updateComponentTypes(selectedType) {
	    fetch(`/shipyard/component-types/${selectedType}`)
	        .then(response => response.json())
	        .then(data => {
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

	typeSelect.addEventListener('change', function() {
	    updateComponentTypes(this.value);
	});

	// Initialize with default type on load
	updateComponentTypes(typeSelect.value);

    function revertEditMode(row, attributeName, attributeValue) {
        row.classList.remove('edit-mode');
        row.querySelector('.attribute-name').textContent = attributeName;
        row.querySelector('.attribute-value').textContent = attributeValue;
        row.querySelector('.attribute-actions').innerHTML = '<button class="edit-btn">Edit</button>';

        // Re-attach event listener to new edit button
        row.querySelector('.edit-btn').addEventListener('click', function (e) {
			e.stopPropagation();
            triggerEditMode(row);
        });
    }

    function triggerEditMode(row) {
		const attributeName = row.querySelector('.attribute-name').textContent;
		const attributeNameOptions = row.querySelector('.attribute-name').getAttribute('attribute-name-options');
		const attributeValue = row.querySelector('.attribute-value').textContent;
		
		row.classList.add('edit-mode');
			
		const select = document.createElement('select');
		select.className = 'edit-name';
		for (option of attributeNameOptions.split('-')) {
			option = option.trim();
			const opt = document.createElement('option');
			opt.value = option;
			opt.textContent = option;
			if (option === attributeName) opt.selected = true;
			select.appendChild(opt);
		}
		row.querySelector('.attribute-name').textContent = '';
		row.querySelector('.attribute-name').appendChild(select);
					
		row.querySelector('.attribute-value').innerHTML = `<input type="number" class="edit-value" value="${attributeValue}">`;
		row.querySelector('.attribute-actions').innerHTML = `
			<button class="cancel-btn">✕</button>
			<button class="confirm-btn">✓</button>
		`;

		// Cancel button handler
		row.querySelector('.cancel-btn').addEventListener('click', function (e) {
			e.stopPropagation();
			revertEditMode(row, attributeName, attributeValue);
		});

		// Confirm button handler
		row.querySelector('.confirm-btn').addEventListener('click', function (e) {
		    e.stopPropagation();
		    const newAttributeName = row.querySelector('.edit-name').value;
		    const newAttributeValue = row.querySelector('.edit-value').value;
		    revertEditMode(row, newAttributeName, newAttributeValue);
		});
    }

    // Back button handlers
    backBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            const isFromOverview = this.parentElement.classList.contains('component-overview-view');
            if (isFromOverview) {
                componentsView.classList.remove('slide-out');
                overviewView.classList.remove('slide-in');
            } else {
                overviewView.classList.remove('slide-out');
                levelDetailsView.classList.remove('slide-in');
            }
        });
    });
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
            document.getElementById('component-response-label').textContent = 'Component created successfully!';
            document.getElementById('component-response-label').style.color = 'green';
            reloadModal(shipname);
        } else {
            document.getElementById('component-response-label').textContent = 'Error: ' + data;
            document.getElementById('component-response-label').style.color = 'red';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('component-response-label').textContent = 'Failed to create component.';
    });
	
}

function triggerComponentEditMode(row) {
    const componentType = row.getAttribute('data-component-name');
    const componentSubtype = row.querySelector('td:nth-child(3)').textContent;
    const shipname = row.getAttribute('data-shipname');
    const id = row.getAttribute('data-component-id');

    // Fetch options for subtype dropdown
    fetch(`/shipyard/component-types/${componentType}`)
        .then(response => response.json())
        .then(options => {
            row.classList.add('edit-mode');
            const select = document.createElement('select');
            select.className = 'edit-type';
            options.forEach(option => {
                const opt = document.createElement('option');
                opt.value = option;
                opt.textContent = option;
                if (option === componentSubtype) opt.selected = true;
                select.appendChild(opt);
            });
            row.querySelector('td:nth-child(3)').innerHTML = '';
            row.querySelector('td:nth-child(3)').appendChild(select);
            if (componentType === 'CORE') {
				row.querySelector('.attribute-actions').innerHTML = `
				                <button class="cancel-btn">✕</button>
				                <button class="confirm-btn">✓</button>
				            `;
            } else {
				row.querySelector('.attribute-actions').innerHTML = `
								<button class="cancel-btn">✕</button>
								<button class="confirm-btn">✓</button>
								<button class="delete-btn">🗑️</button>
							`;
			}

            // Cancel handler
            row.querySelector('.cancel-btn').addEventListener('click', function (e) {
                e.stopPropagation();
                revertComponentEditMode(row, componentSubtype);
            });

            // Confirm handler
            row.querySelector('.confirm-btn').addEventListener('click', function (e) {
                e.stopPropagation();
                const newSubtype = row.querySelector('.edit-type').value;
                fetch('/shipyard/update/component', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ shipname, id, type: newSubtype })
                }).then(() => {
                    revertComponentEditMode(row, newSubtype);
					reloadModal(shipname);
                });
            });

            // Delete handler
            row.querySelector('.delete-btn').addEventListener('click', function (e) {
                e.stopPropagation();
                if (confirm('Delete this component?')) {
                    fetch('/shipyard/delete/component', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ shipname, id })
                    }).then(() => {
                        reloadModal(shipname);
                    });
                }
            });
        });
}

function revertComponentEditMode(row, subtype) {
    row.classList.remove('edit-mode');
    row.querySelector('td:nth-child(3)').textContent = subtype;
    row.querySelector('.attribute-actions').innerHTML = '<button class="edit-btn">Edit</button>';
    row.querySelector('.edit-btn').addEventListener('click', function (e) {
        e.stopPropagation();
        triggerComponentEditMode(row);
    });
}

function reloadModal(shipname) {
	fetch(`/shipyard/modal/shipoverviewmodal/${shipname}`)
	   .then(response => response.text())
	   .then(html => {
	   		const modalContent = document.querySelector('.modal-content');
	    	const closeBtn = modalContent.querySelector('.close');
	    	closeBtn.nextElementSibling.innerHTML = html;
	    	if (typeof initializeComponentRows === 'function') {
	        	initializeComponentRows();
	    	}
	 });
}