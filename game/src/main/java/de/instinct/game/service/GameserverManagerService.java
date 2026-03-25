package de.instinct.game.service;

import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.game.dto.MapPreview;
import de.instinct.api.matchmaking.model.FactionMode;

public interface GameserverManagerService {

	void start();
	
	void stop();
	
	String createSession(GameSessionInitializationRequest request);

	MapPreview preview(FactionMode mode, String map);

}
