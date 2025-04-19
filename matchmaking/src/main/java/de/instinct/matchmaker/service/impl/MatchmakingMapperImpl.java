package de.instinct.matchmaker.service.impl;

import org.springframework.stereotype.Service;

import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.service.MatchmakingMapper;
import de.instinct.matchmaker.service.model.GameType;

@Service
public class MatchmakingMapperImpl implements MatchmakingMapper {

	@Override
	public GameType map(MatchmakingRegistrationRequest request) {
		return GameType.builder()
				.factionMode(request.factionMode)
				.gameMode(request.gameMode)
				.versusMode(request.versusMode)
				.build();
	}

}
