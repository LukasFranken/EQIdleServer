package de.instinct.api.shipyard.service;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shipyard.dto.ShipAddResponse;
import de.instinct.api.shipyard.dto.ShipBuildResponse;
import de.instinct.api.shipyard.dto.ShipUpgradeResponse;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.StatChangeResponse;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;

public interface ShipyardInterface extends BaseServiceInterface {
	
	ShipyardInitializationResponseCode init(String token);
	
	PlayerShipyardData data(String token);
	
	ShipyardData shipyard();
	
	UseShipResponseCode use(String token, String shipUUID);
	
	UnuseShipResponseCode unuse(String token, String shipUUID);
	
	StatChangeResponse hangar(String token, int count);

	StatChangeResponse active(String token, int count);
	
	ShipBuildResponse build(String shiptoken);
	
	ShipUpgradeResponse upgrade(String shiptoken);
	
	ShipAddResponse add(String token, int shipid);

}
