package de.instinct.matchmaking.service;

import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.InviteResponse;
import de.instinct.api.matchmaking.dto.InvitesStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyCreationResponse;
import de.instinct.api.matchmaking.dto.LobbyStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyTypeSetResponse;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationRequest;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponseCode;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.api.matchmaking.model.GameType;

public interface MatchmakingService {

	LobbyCreationResponse createLobby(String authToken);
	
	LobbyTypeSetResponse setType(String authToken, String lobbyUUID, GameType selectedGameType);
	
	InviteResponse invite(String authToken, String username);
	
	void respond(String authToken, String lobbyUUID, boolean accepted);
	
	InvitesStatusResponse getInvites(String authToken);
	
	MatchmakingRegistrationResponseCode start(String playerAuthToken, MatchmakingRegistrationRequest request);

	LobbyStatusResponse getStatus(String lobbyToken);
	
	MatchmakingStatusResponse getMatchmakingStatus(String lobbyUUID);

	void callback(String lobbyToken, CallbackCode code);
	
	void finish(String lobbyToken);

	void dispose(String lobbyToken);

}
