package de.instinct.game.service.impl;

import org.springframework.stereotype.Service;

import base.game.dto.request.GameserverInitializationRequest;
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
