package de.instinct.api.shipyard.service;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.service.impl.ShipyardInitializationResponseCode;

public interface ShipyardInterface extends BaseServiceInterface {
	
	ShipyardInitializationResponseCode init(String token);
	
	ShipyardData data(String token);

}
