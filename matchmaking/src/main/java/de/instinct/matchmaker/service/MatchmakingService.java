package de.instinct.matchmaker.service;

import de.instinct.matchmaker.controller.dto.CallbackCode;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponse;

public interface MatchmakingService {

	MatchmakingRegistrationResponse register(String playerAuthToken, MatchmakingRegistrationRequest request);

	MatchmakingStatusResponse getStatus(String lobbyToken);

	void callback(String lobbyToken, CallbackCode code);

	void dispose(String lobbyToken);

}
