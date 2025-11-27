if (typeof window !== 'undefined') {
    document.addEventListener('DOMContentLoaded', function () {
		document.querySelectorAll('[modal]').forEach(btn => {
			btn.addEventListener('click', function () {
				loadModal(this.getAttribute('modal'));
			});
		});
        document.querySelectorAll('[data-modal]').forEach(btn => {
            btn.addEventListener('click', function () {
                loadDataModal(this.getAttribute('data-modal').split('-')[0], this.getAttribute('data-modal').split('-')[1]);
            });
        });
		document.querySelectorAll('[param-modal]').forEach(btn => {
			btn.addEventListener('click', function () {
				loadParamModal(this.getAttribute('param-modal').split('-')[0], this.getAttribute('param-modal').split('-')[1], this.getAttribute('param'));
			});
		});
    });
	
	function loadParamModal(controller, modalName, param) {
		var endpoint = `/${controller}/modal/${modalName}/${param}`;
		fetchModal(endpoint);
	}
	
	function loadDataModal(controller, modalName) {
		var endpoint = `/${controller}/modal/${modalName}`;
		fetchModal(endpoint);
	}
	
	function loadModal(modalName) {
		var endpoint = `/modal/${modalName}`;
		fetchModal(endpoint);
	}

	function fetchModal(endpoint) {
        fetch(endpoint, { method: 'GET' })
            .then(response => response.text())
            .then(html => {
				const modalContent = document.querySelector('.modal-content');
				const closeBtn = modalContent.querySelector('.close');
				closeBtn.nextElementSibling.innerHTML = html;
				toggleModal(true);
				if (typeof initializeComponentRows === 'function') {
					initializeComponentRows();
				}
            })
            .catch(error => console.error('Error loading modal:', error));
    }
	
    function toggleModal(show) {
        var modal = document.getElementById('modal');
        var body = document.getElementsByTagName('body')[0];
        if (show) {
            modal.style.display = 'flex';
            body.classList.add('modal-open');
        } else {
            modal.style.display = 'none';
            body.classList.remove('modal-open');
        }
    }
}
