package de.instinct.engine_api.mining.service;

import de.instinct.api.mining.service.MiningInterface;
import de.instinct.engine_api.mining.model.MiningPlayerInventoryData;
import de.instinct.engine_api.mining.service.model.MiningMissionOverview;

public interface MiningEngineInterface extends MiningInterface {
	
	MiningPlayerInventoryData inventory(String token);
	
	MiningMissionOverview mission(String missionName);

}
