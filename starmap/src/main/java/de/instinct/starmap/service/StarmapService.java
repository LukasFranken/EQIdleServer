package de.instinct.starmap.service;

import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.CompletionResponse;
import de.instinct.api.starmap.dto.PlayerStarmapData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;

public interface StarmapService {
	
	StarmapInitializationResponseCode init(String token);

	PlayerStarmapData getStarmapData(String token);
	
	SectorData getSectorData();

	CompletionResponse completeSystem(CompletionRequest request);

}
