package de.instinct.api.shipyard.service.impl;

import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.shipyard.service.ShipyardInterface;

public class Shipyard extends BaseService implements ShipyardInterface {

	public Shipyard() {
		super("shipyard");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}

}
