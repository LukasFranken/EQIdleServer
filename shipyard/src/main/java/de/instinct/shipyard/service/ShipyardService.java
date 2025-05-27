package de.instinct.shipyard.service;

import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.service.impl.ShipyardInitializationResponseCode;

public interface ShipyardService {

	ShipyardInitializationResponseCode init(String token);
	
	ShipyardData getShipyardData(String token);

}
