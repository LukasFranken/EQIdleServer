package de.instinct.game.service;

import base.game.dto.request.GameserverInitializationRequest;

public interface GameserverManagerService {

	void start(GameserverInitializationRequest request);

}
