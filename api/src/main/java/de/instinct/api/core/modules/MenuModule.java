package de.instinct.api.core.modules;

public enum MenuModule {
	
	PLAY("CONTRACTS"),
	LOADOUT("LOADOUT"),
	SETTINGS("SETTINGS"),
	PROFILE("PROFILE"),
	INVENTORY("INVENTORY"),
	MARKET("MARKET"),
	SHOP("SHOP"),
	SHIPYARD("SHIPYARD"),
	CONSTRUCTION("CONSTRUCTION");
	
	String label;
	
	MenuModule(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}

}
