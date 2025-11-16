function initializeComponentRows() {
    const container = document.querySelector('.modal-view-container');
    if (!container) return;

    const tableView = container.querySelector('.components-table-view');
    const overviewView = container.querySelector('.component-overview-view');
    const levelDetailsView = container.querySelector('.level-details-view');
    const componentTitle = overviewView.querySelector('.component-title');
    const levelTitle = levelDetailsView.querySelector('.level-title');
    const backBtns = container.querySelectorAll('.back-btn');

    // Component row click handler
    document.querySelectorAll('.component-row').forEach(row => {
        row.addEventListener('click', function () {
            const componentName = this.getAttribute('data-component');
            componentTitle.textContent = componentName;
            tableView.classList.add('slide-out');
            overviewView.classList.add('slide-in');
        });
    });

    // Level row click handler
    document.querySelectorAll('.level-row').forEach(row => {
        row.addEventListener('click', function () {
            const level = this.getAttribute('data-level');
            const type = this.getAttribute('data-type');
            const value = this.getAttribute('data-value');
            levelTitle.textContent = `Level ${level} - ${type}: ${value}`;
            overviewView.classList.add('slide-out');
            levelDetailsView.classList.add('slide-in');
        });
    });

    // Attribute row edit button handler
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.stopPropagation();
            const row = this.closest('.attribute-row');
            const attributeName = row.querySelector('.attribute-name').textContent;
            const attributeValue = row.querySelector('.attribute-value').textContent;

            row.classList.add('edit-mode');
            row.querySelector('.attribute-name').innerHTML = `<input type="text" class="edit-name" value="${attributeName}">`;
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
                revertEditMode(row, attributeName, attributeValue);
            });
        });
    });

    function revertEditMode(row, attributeName, attributeValue) {
        row.classList.remove('edit-mode');
        row.querySelector('.attribute-name').textContent = attributeName;
        row.querySelector('.attribute-value').textContent = attributeValue;
        row.querySelector('.attribute-actions').innerHTML = '<button class="edit-btn">Edit</button>';

        // Re-attach event listener to new edit button
        row.querySelector('.edit-btn').addEventListener('click', function (e) {
            e.stopPropagation();
            const nameText = row.querySelector('.attribute-name').textContent;
            const valueText = row.querySelector('.attribute-value').textContent;
            triggerEditMode(row, nameText, valueText);
        });
    }

    function triggerEditMode(row, attributeName, attributeValue) {
        row.classList.add('edit-mode');
        row.querySelector('.attribute-name').innerHTML = `<input type="text" class="edit-name" value="${attributeName}">`;
        row.querySelector('.attribute-value').innerHTML = `<input type="number" class="edit-value" value="${attributeValue}">`;
        row.querySelector('.attribute-actions').innerHTML = `
            <button class="cancel-btn">✕</button>
            <button class="confirm-btn">✓</button>
        `;

        row.querySelector('.cancel-btn').addEventListener('click', function (e) {
            e.stopPropagation();
            revertEditMode(row, attributeName, attributeValue);
        });

        row.querySelector('.confirm-btn').addEventListener('click', function (e) {
            e.stopPropagation();
            revertEditMode(row, attributeName, attributeValue);
        });
    }

    // Back button handlers
    backBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            const isFromOverview = this.parentElement.classList.contains('component-overview-view');
            if (isFromOverview) {
                tableView.classList.remove('slide-out');
                overviewView.classList.remove('slide-in');
            } else {
                overviewView.classList.remove('slide-out');
                levelDetailsView.classList.remove('slide-in');
            }
        });
    });
}
