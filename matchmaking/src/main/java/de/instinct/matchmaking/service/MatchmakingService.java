package de.instinct.matchmaking.service;

import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationRequest;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponse;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;

public interface MatchmakingService {

	MatchmakingRegistrationResponse register(String playerAuthToken, MatchmakingRegistrationRequest request);

	MatchmakingStatusResponse getStatus(String lobbyToken);

	void callback(String lobbyToken, CallbackCode code);

	void dispose(String lobbyToken);

}
