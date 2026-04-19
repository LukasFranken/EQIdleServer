package de.instinct.api.mining.service;

import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;

public interface MiningInterface {

	void start();
	
	CreateSessionResponse createSession(CreateSessionRequest request);
	
}
