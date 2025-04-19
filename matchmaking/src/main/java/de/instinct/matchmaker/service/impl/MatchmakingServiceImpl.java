package de.instinct.matchmaker.service.impl;

import org.springframework.stereotype.Service;

import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponse;
import de.instinct.matchmaker.service.MatchmakingService;

@Service
public class MatchmakingServiceImpl implements MatchmakingService {
	
	@Override
	public MatchmakingRegistrationResponse register(String playerAuthToken, MatchmakingRegistrationRequest request) {
		return null;
	}

	@Override
	public MatchmakingStatusResponse getStatus(String playerAuthToken) {
		return null;
	}

}
