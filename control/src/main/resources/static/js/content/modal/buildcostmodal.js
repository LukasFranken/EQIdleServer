function initializeBuildCostModal() {
    document.querySelectorAll('.buildcost-row .edit-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.stopPropagation();
            const row = this.closest('.buildcost-row');
            triggerBuildCostEditMode(row);
        });
    });
}

function triggerBuildCostEditMode(row) {
    fetch(`/shipyard/resource-types`)
        .then(response => response.json())
        .then(options => {
            updateBuildCostEditSection(row, options);
        });
}

function updateBuildCostEditSection(row, options) {
    const id = row.querySelector('td:nth-child(1)').textContent;
    const currentType = row.querySelector('td:nth-child(2)').textContent;
    const value = row.querySelector('td:nth-child(3)').textContent;
    const shipname = document.getElementById('shipname').textContent
    row.classList.add('edit-mode');
    createSelectElement(row, options, currentType, 2);
    createValueInputElement(row, value, 3);
    row.querySelector('.buildcost-actions').innerHTML = `
											<button class="cancel-btn">✕</button>
											<button class="confirm-btn">✓</button>
											<button class="delete-btn">🗑️</button>
										`;

    // Cancel handler
    row.querySelector('.cancel-btn').addEventListener('click', function(e) {
        e.stopPropagation();
        revertBuildCostEditMode(row, currentType, value);
    });

    // Confirm handler
    row.querySelector('.confirm-btn').addEventListener('click', function(e) {
        e.stopPropagation();
        const resourceType = row.querySelector('.edit-type').value;
        const cost = row.querySelector('.edit-value').value;
        fetch('/shipyard/update/buildcost', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ shipname, id, resourceType, cost })
        }).then(() => {
            revertBuildCostEditMode(row, resourceType, cost);
            updateBuildCosts(shipname);
        });
    });

    // Delete handler
    const rowDeleteBtn = row.querySelector('.delete-btn');
    if (rowDeleteBtn !== null) {
        rowDeleteBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            if (confirm('Delete buildcost with ID ' + id + '?')) {
                fetch('/shipyard/delete/buildcost', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ shipname, id })
                }).then(() => {
                    updateBuildCosts(shipname);
                });
            }
        });
    }
}

function revertBuildCostEditMode(row, type, value) {
    row.classList.remove('edit-mode');
    row.querySelector('td:nth-child(2)').textContent = type;
    row.querySelector('td:nth-child(3)').textContent = value;
    row.querySelector('.buildcost-actions').innerHTML = '<button class="edit-btn">Edit</button>';
    row.querySelector('.edit-btn').addEventListener('click', function(e) {
        e.stopPropagation();
        triggerBuildCostEditMode(row);
    });
}

function addBuildCost(shipname) {
    const request = { shipname };

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
    const endpoint = `/shipyard/modal/buildcostmodal/${shipname}`;

    fetch(endpoint, { method: 'GET' })
        .then(response => response.text())
        .then(html => {
            const modalContent = document.querySelector('.buildcostmodal-content');
            modalContent.innerHTML = html;

            if (typeof initializeBuildCostModal === 'function') {
                initializeBuildCostModal();
            }
        })
        .catch(error => console.error('Error refreshing build cost modal:', error));
}
