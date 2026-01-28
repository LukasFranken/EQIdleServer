const deleteBtn = document.getElementById('delete-category-btn');
if (deleteBtn) {
    deleteBtn.addEventListener('click', () => {
        const categoryId = deleteBtn.dataset.categoryId;
        deleteCategory(categoryId);
    });
}

function refreshShop() {
    fetch('/shop/refresh', { method: 'GET' })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                console.error('Failed to refresh');
            }
        })
        .catch(error => console.error('Error:', error));
}

function deleteCategory(categoryId) {
    if (confirm('Delete category with ID ' + categoryId + '?')) {
        fetch('/shop/delete/category/' + categoryId, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        }).then(() => {
            refreshShop();
        });
    }
}