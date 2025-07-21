package de.instinct.shipyard.service;

import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shipyard.dto.ShipBuildResponse;
import de.instinct.api.shipyard.dto.ShipUpgradeResponse;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.StatChangeResponse;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;

public interface ShipyardService {

	ShipyardInitializationResponseCode init(String token);
	
	PlayerShipyardData getShipyardData(String token);
	
	ShipyardData getBaseData();

	UseShipResponseCode useShip(String token, String shipUUID);
	
	UnuseShipResponseCode unuseShip(String token, String shipUUID);
	
	StatChangeResponse changeHangarSpace(String token, int count);

	StatChangeResponse changeActiveShips(String token, int count);

	ShipBuildResponse build(String token, String shiptoken);

	ShipUpgradeResponse upgrade(String token, String shiptoken);

}
