package de.instinct.api.starmap.service;

import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.CompletionResponse;
import de.instinct.api.starmap.dto.PlayerStarmapData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.SectorDataRequest;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;
import de.instinct.api.starmap.dto.StartConquestRequest;
import de.instinct.api.starmap.dto.StartConquestResponse;

public interface StarmapInterface {
	
	StarmapInitializationResponseCode init(String token);
	
	String load();

	PlayerStarmapData data(String token);
	
	SectorData sector(SectorDataRequest request);

	CompletionResponse complete(CompletionRequest request);

	StartConquestResponse start(StartConquestRequest request);

}
