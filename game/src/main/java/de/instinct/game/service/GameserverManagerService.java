package de.instinct.game.service;

import de.instinct.api.game.dto.GameserverInitializationRequest;

public interface GameserverManagerService {

	void start(GameserverInitializationRequest request);

}
