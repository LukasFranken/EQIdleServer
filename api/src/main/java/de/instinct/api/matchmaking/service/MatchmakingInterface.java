package de.instinct.api.matchmaking.service;

import de.instinct.api.core.model.GeneralRequestResponse;
import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationRequest;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponse;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;

public interface MatchmakingInterface extends BaseServiceInterface {
	
	MatchmakingRegistrationResponse register(MatchmakingRegistrationRequest request);
	
	MatchmakingStatusResponse status(String lobbyUUID);
	
	GeneralRequestResponse callback(String lobbyUUID, CallbackCode code);
	
	GeneralRequestResponse dispose(String lobbyUUID);

}
