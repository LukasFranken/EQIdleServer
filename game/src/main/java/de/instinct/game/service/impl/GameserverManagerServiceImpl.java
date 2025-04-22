package de.instinct.game.service.impl;

import org.springframework.stereotype.Service;

import de.instinct.api.game.dto.GameserverInitializationRequest;
import de.instinct.game.service.GameserverManagerService;

@Service
public class GameserverManagerServiceImpl implements GameserverManagerService {

	@Override
	public void start(GameserverInitializationRequest request) {
		//load game data
		//start gameserver
		//send callback to matchmaking
	}

}
