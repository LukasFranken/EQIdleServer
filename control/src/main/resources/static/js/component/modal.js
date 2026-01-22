if (typeof window !== 'undefined') {
	document.querySelectorAll('[param-modal]').forEach(btn => {
		btn.addEventListener('click', function() {
			loadParamModal(this.getAttribute('param-modal').split('-')[0], this.getAttribute('param-modal').split('-')[1], this.getAttribute('param'), this.getAttribute('init-method'));
		});
	});

	document.querySelectorAll('[data-modal]').forEach(btn => {
		btn.addEventListener('click', function() {
			loadDataModal(this.getAttribute('data-modal').split('-')[0], this.getAttribute('data-modal').split('-')[1], this.getAttribute('init-method'));
		});
	});

	document.querySelectorAll('[modal]').forEach(btn => {
		btn.addEventListener('click', function() {
			loadModal(this.getAttribute('modal'), this.getAttribute('init-method'));
		});
	});
}

function loadParamModal(controller, modalName, param, initMethod) {
	var endpoint = `/${controller}/modal/${modalName}/${param}`;
	fetchModal(endpoint, initMethod);
}

function loadDataModal(controller, modalName, initMethod) {
	var endpoint = `/${controller}/modal/${modalName}`;
	fetchModal(endpoint, initMethod);
}

function loadModal(modalName, initMethod) {
	var endpoint = `/modal/${modalName}`;
	fetchModal(endpoint, initMethod);
}

function fetchModal(endpoint, initMethod) {
	fetch(endpoint, { method: 'GET' })
		.then(response => response.text())
		.then(html => {
			const modalContent = document.querySelector('.modal-content');
			const closeBtn = modalContent.querySelector('.close');
			closeBtn.nextElementSibling.innerHTML = html;
			toggleModal(true);

			if (initMethod && typeof window[initMethod] === 'function') {
				window[initMethod]();
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
