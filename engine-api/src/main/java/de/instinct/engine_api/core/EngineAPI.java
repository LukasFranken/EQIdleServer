package de.instinct.engine_api.core;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.engine_api.fleet.ship.service.impl.ShipyardEngine;

public class EngineAPI extends API {
	
	private static ShipyardEngine shipyard;
	
	public static void initialize(APIConfiguration newConfiguration) {
		shipyard = new ShipyardEngine();
		if (newConfiguration != APIConfiguration.SERVER) {
			shipyard().connect(); 
		}
	}
	
	public static ShipyardEngine shipyard() {
		if (!API.apiReady()) return null;
		return shipyard;
	}

}
