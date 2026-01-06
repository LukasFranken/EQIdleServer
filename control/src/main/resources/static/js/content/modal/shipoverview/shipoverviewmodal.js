function initializeBlueprintModal() {
	const container = document.querySelector('.modal-view-container');
	if (!container) return;
	console.log('Initializing Ship Overview Modal');
	
	initializeComponents();
	initializeComponentLevels();
	initializeLevelAttributes();
}
