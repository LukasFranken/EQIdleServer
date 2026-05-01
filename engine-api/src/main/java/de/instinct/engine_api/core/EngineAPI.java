package de.instinct.engine_api.core;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.engine_api.fleet.ship.service.impl.ShipyardEngine;
import de.instinct.engine_api.mining.service.impl.MiningEngine;

public class EngineAPI extends API {
	
	private static ShipyardEngine shipyard;
	private static MiningEngine mining;
	
	public static void initialize(APIConfiguration newConfiguration) {
		shipyard = new ShipyardEngine();
		mining = new MiningEngine();
		if (newConfiguration != APIConfiguration.SERVER) {
			shipyard().connect(); 
			mining().connect();
		}
	}
	
	public static ShipyardEngine shipyard() {
		if (!API.apiReady()) return null;
		return shipyard;
	}
	
	public static MiningEngine mining() {
		if (!API.apiReady()) return null;
		return mining;
	}

}
