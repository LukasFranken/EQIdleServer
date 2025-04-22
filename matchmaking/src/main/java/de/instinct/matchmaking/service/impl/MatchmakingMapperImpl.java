package de.instinct.matchmaking.service.impl;

import org.springframework.stereotype.Service;

import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponseCode;
import de.instinct.matchmaking.service.MatchmakingMapper;
import de.instinct.matchmaking.service.model.GameserverInfo;

@Service
public class MatchmakingMapperImpl implements MatchmakingMapper {

	@Override
	public MatchmakingStatusResponse mapGameserverInfo(MatchmakingStatusResponse response, GameserverInfo gameserverInfo) {
		switch (gameserverInfo.getStatus()) {
		case NOT_CREATED:
			response.setCode(MatchmakingStatusResponseCode.MATCHING);
			break;
		case IN_CREATION:
			response.setCode(MatchmakingStatusResponseCode.IN_GAMESERVER_CREATION);
			break;
		case READY:
			response.setCode(MatchmakingStatusResponseCode.READY);
			response.setGameserverAddress(gameserverInfo.getAddress());
			response.setGameserverPort(gameserverInfo.getPort());
			break;
		default:
			response.setCode(MatchmakingStatusResponseCode.ERROR);
			break;
		}
		return response;
	}

}
