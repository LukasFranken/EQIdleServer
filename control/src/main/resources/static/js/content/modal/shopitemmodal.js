function initializeShopItemModal() {
    document.querySelectorAll('.shop-item-stage-row .edit-btn').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.stopPropagation();
            const row = this.closest('.shop-item-stage-row');
            triggerShopItemStageEditMode(row);
        });
    });
}

function triggerShopItemStageEditMode(row) {
    fetch(`/shop/effect-types`)
        .then(response => response.json())
        .then(options => {
            updateShopItemStageEditSection(row, options);
        });
}

function updateShopItemStageEditSection(row, options) {
    const id = row.querySelector('td:nth-child(1)').textContent;
    const price = row.querySelector('td:nth-child(2)').textContent;
    const description = row.querySelector('td:nth-child(3)').textContent;
	const type = row.querySelector('td:nth-child(4)').textContent;
	const data = row.querySelector('td:nth-child(5)').textContent;
    const itemId = document.getElementById('id').textContent
    row.classList.add('edit-mode');
	
	createValueInputElement(row, price, 2);
    createInputElement(row, description, 3);
	createSelectElement(row, options, type, 4);
	createInputElement(row, data, 5);
    row.querySelector('.shop-item-stage-actions').innerHTML = `
												<button class="cancel-btn">✕</button>
												<button class="confirm-btn">✓</button>
												<button class="delete-btn">🗑️</button>
											`;

    // Cancel handler
    row.querySelector('.cancel-btn').addEventListener('click', function(e) {
        e.stopPropagation();
        revertShopItemStageEditMode(row, price, description, type, data);
    });

    // Confirm handler
    row.querySelector('.confirm-btn').addEventListener('click', function(e) {
        e.stopPropagation();
		const newPrice = row.querySelector('td:nth-child(2) input.edit-value')?.value;
		const newDescription = row.querySelector('td:nth-child(3) input.edit-value')?.value;
		const newType = row.querySelector('td:nth-child(4) select.edit-type')?.value;
		const newData = row.querySelector('td:nth-child(5) input.edit-value')?.value;
        fetch('/shop/update/itemstage', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ itemId, id, newPrice, newDescription, newType, newData })
        }).then(() => {
            revertShopItemStageEditMode(row, price, description, type, data);
            updateShopItemStage(itemId);
        });
    });

    // Delete handler
    const rowDeleteBtn = row.querySelector('.delete-btn');
    if (rowDeleteBtn !== null) {
        rowDeleteBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            if (confirm('Delete stage with ID ' + id + '?')) {
                fetch('/shop/delete/itemstage', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ itemId, id })
                }).then(() => {
                    updateShopItemStage(itemId);
                });
            }
        });
    }
}

function revertShopItemStageEditMode(row, price, description, type, data) {
    row.classList.remove('edit-mode');
    row.querySelector('td:nth-child(2)').textContent = price;
    row.querySelector('td:nth-child(3)').textContent = description;
    row.querySelector('td:nth-child(4)').textContent = type;
    row.querySelector('td:nth-child(5)').textContent = data;
    row.querySelector('.shop-item-stage-actions').innerHTML = '<button class="edit-btn">Edit</button>';
    row.querySelector('.edit-btn').addEventListener('click', function(e) {
        e.stopPropagation();
        triggerShopItemStageEditMode(row);
    });
}

function addShopItemStage(id) {
    const request = { id };
    fetch('/shop/create/itemstage', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(request)
    })
        .then(response => response.json())
        .then(data => {
            if (data === 'SUCCESS') {
                updateShopItemStage(id);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateShopItemStage(id) {
    const endpoint = `/shop/modal/shopitemmodal/${id}`;

    fetch(endpoint, { method: 'GET' })
        .then(response => response.text())
        .then(html => {
            const modalContent = document.querySelector('.shopitemmodal-content');
            modalContent.innerHTML = html;

            if (typeof initializeBuildCostModal === 'function') {
                initializeShopItemModal();
            }
        })
        .catch(error => console.error('Error refreshing build cost modal:', error));
}