package de.instinct.shipyard.service;

import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shipyard.dto.ShipAddResponse;
import de.instinct.api.shipyard.dto.ShipBuildResponse;
import de.instinct.api.shipyard.dto.ShipUpgradeResponse;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.StatChangeResponse;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;
import de.instinct.api.shipyard.dto.admin.ComponentCreateRequest;
import de.instinct.api.shipyard.dto.admin.ComponentCreateResponse;
import de.instinct.api.shipyard.dto.admin.ComponentDeleteRequest;
import de.instinct.api.shipyard.dto.admin.ComponentDeleteResponse;
import de.instinct.api.shipyard.dto.admin.ShipCreateRequest;
import de.instinct.api.shipyard.dto.admin.ShipCreateResponse;

public interface ShipyardService {

	ShipyardInitializationResponseCode init(String token);
	
	PlayerShipyardData getShipyardData(String token);
	
	ShipyardData getBaseData();
	
	void loadBaseData();

	UseShipResponseCode useShip(String token, String shipUUID);
	
	UnuseShipResponseCode unuseShip(String token, String shipUUID);
	
	StatChangeResponse changeHangarSpace(String token, int count);

	StatChangeResponse changeActiveShips(String token, int count);

	ShipBuildResponse build(String token, String shiptoken);

	ShipUpgradeResponse upgrade(String token, String shiptoken);

	ShipAddResponse addBlueprint(String token, int shipid);

	ShipCreateResponse createShip(ShipCreateRequest request);
	
	ComponentCreateResponse createComponent(ComponentCreateRequest request);

	ComponentDeleteResponse deleteComponent(ComponentDeleteRequest request);

}
