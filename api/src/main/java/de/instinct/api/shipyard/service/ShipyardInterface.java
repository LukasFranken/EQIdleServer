package de.instinct.api.shipyard.service;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;

public interface ShipyardInterface extends BaseServiceInterface {
	
	ShipyardInitializationResponseCode init(String token);
	
	ShipyardData data(String token);
	
	UseShipResponseCode use(String token, String shipUUID);
	
	UnuseShipResponseCode unuse(String token, String shipUUID);

}
