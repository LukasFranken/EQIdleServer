package de.instinct.api.starmap.service;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;

public interface StarmapInterface extends BaseServiceInterface {
	
	StarmapInitializationResponseCode init(String token);

	SectorData data(String token);

}
