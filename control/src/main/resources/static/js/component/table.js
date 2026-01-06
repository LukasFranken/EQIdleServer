function createValueInputElement(row, value, column) {
	const input = document.createElement('input');
	input.type = 'number';
	input.className = 'edit-value';
	input.value = value;
	row.querySelector('td:nth-child(' + column + ')').innerHTML = '';
	row.querySelector('td:nth-child(' + column + ')').appendChild(input);
}

function createSelectElement(row, options, selected, column) {
	const select = document.createElement('select');
	select.className = 'edit-type';
	options.forEach(option => {
		const opt = document.createElement('option');
		opt.value = option;
		opt.textContent = option;
		if (selected !== null && option === selected) opt.selected = true;
		select.appendChild(opt);
	});
	row.querySelector('td:nth-child(' + column + ')').innerHTML = '';
	row.querySelector('td:nth-child(' + column + ')').appendChild(select);
}