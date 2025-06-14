package de.instinct.matchmaking.service;

import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.InviteResponse;
import de.instinct.api.matchmaking.dto.InvitesStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyCreationResponse;
import de.instinct.api.matchmaking.dto.LobbyLeaveResponse;
import de.instinct.api.matchmaking.dto.LobbyStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyTypeSetResponse;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponseCode;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.api.matchmaking.dto.MatchmakingStopResponseCode;
import de.instinct.api.matchmaking.model.GameType;

public interface MatchmakingService {

	LobbyCreationResponse createLobby(String authToken);
	
	LobbyLeaveResponse leaveLobby(String authToken);
	
	String getUserLobby(String authToken);
	
	LobbyTypeSetResponse setType(String authToken, String lobbyUUID, GameType selectedGameType);
	
	InviteResponse invite(String authToken, String username);
	
	String respond(String authToken, String lobbyUUID, boolean accepted);
	
	InvitesStatusResponse getInvites(String authToken);
	
	MatchmakingRegistrationResponseCode start(String playerAuthToken, String lobbyUUID);

	LobbyStatusResponse getStatus(String lobbyToken);
	
	MatchmakingStatusResponse getMatchmakingStatus(String lobbyUUID);
	
	MatchmakingStopResponseCode stop(String authToken, String lobbyUUID);

	void callback(String gamesessionUUID, CallbackCode code);
	
	void finish(String gamesessionUUID);

	void dispose(String gamesessionUUID);

}
