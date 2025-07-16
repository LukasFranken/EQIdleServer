package de.instinct.api.starmap.service;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.CompletionResponse;
import de.instinct.api.starmap.dto.PlayerStarmapData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;

public interface StarmapInterface extends BaseServiceInterface {
	
	StarmapInitializationResponseCode init(String token);
	
	PlayerStarmapData data();
	
	SectorData sector();
	
	CompletionResponse complete(CompletionRequest request);

}
