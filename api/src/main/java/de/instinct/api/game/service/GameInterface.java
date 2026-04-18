package de.instinct.api.game.service;

import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.game.dto.MapPreview;
import de.instinct.api.matchmaking.model.FactionMode;

public interface GameInterface {
	
	void start();
	
	void stop();
	
	String create(GameSessionInitializationRequest request);
	
	MapPreview preview(FactionMode mode, String map);

}
