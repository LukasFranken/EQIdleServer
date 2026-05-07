package de.instinct.api.mining.service;

import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;
import de.instinct.api.mining.dto.Maps;
import de.instinct.api.mining.dto.player.MiningPlayerMissionData;

public interface MiningInterface {

	void start();
	
	CreateSessionResponse createSession(CreateSessionRequest request);
	
	MiningPlayerMissionData missiondata(String token);
	
	Maps maps();
	
}
