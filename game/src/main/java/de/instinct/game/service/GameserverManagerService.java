package de.instinct.game.service;

import de.instinct.api.game.dto.GameSessionInitializationRequest;

public interface GameserverManagerService {

	void start();
	
	void stop();
	
	void createSession(GameSessionInitializationRequest request);

}
