package de.instinct.matchmaker.service.impl;

import org.springframework.stereotype.Service;

import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponseCode;
import de.instinct.matchmaker.service.MatchmakingMapper;
import de.instinct.matchmaker.service.model.GameType;
import de.instinct.matchmaker.service.model.GameserverInfo;

@Service
public class MatchmakingMapperImpl implements MatchmakingMapper {

	@Override
	public GameType map(MatchmakingRegistrationRequest request) {
		return GameType.builder()
				.factionMode(request.getFactionMode())
				.gameMode(request.getGameMode())
				.versusMode(request.getVersusMode())
				.build();
	}

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
