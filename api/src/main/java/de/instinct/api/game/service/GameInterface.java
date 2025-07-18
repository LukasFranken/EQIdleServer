package de.instinct.api.game.service;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.game.dto.MapPreview;

public interface GameInterface extends BaseServiceInterface {
	
	void start();
	
	void stop();
	
	String create(GameSessionInitializationRequest request);
	
	MapPreview preview(String map);

}
