package de.instinct.shipyard.service;

import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;

public interface ShipyardService {

	ShipyardInitializationResponseCode init(String token);
	
	ShipyardData getShipyardData(String token);

	UseShipResponseCode useShip(String token, String shipUUID);

}
