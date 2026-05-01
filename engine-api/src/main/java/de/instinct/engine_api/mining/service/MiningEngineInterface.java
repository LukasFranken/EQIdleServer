package de.instinct.engine_api.mining.service;

import de.instinct.api.mining.service.MiningInterface;
import de.instinct.engine_api.mining.model.MiningPlayerInventoryData;

public interface MiningEngineInterface extends MiningInterface {
	
	MiningPlayerInventoryData getPlayerInventory(String token);

}
