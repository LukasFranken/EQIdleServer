package de.instinct.starmap.service;

import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;

public interface StarmapService {
	
	StarmapInitializationResponseCode init(String token);

	SectorData getStarmapData(String token);

}
