package de.instinct.api.matchmaking.service;

import de.instinct.api.core.model.GeneralRequestResponse;
import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.InviteResponse;
import de.instinct.api.matchmaking.dto.InvitesStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyCreationResponse;
import de.instinct.api.matchmaking.dto.LobbyStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyTypeSetResponse;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponseCode;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.api.matchmaking.model.GameType;

public interface MatchmakingInterface extends BaseServiceInterface {
	
	LobbyCreationResponse create();
	
	String get();
	
	LobbyTypeSetResponse settype(String lobbyUUID, GameType selectedGameType);
	
	InviteResponse invite(String username);
	
	String accept(String lobbyUUID);
	
	String decline(String lobbyUUID);
	
	InvitesStatusResponse invites();
	
	MatchmakingRegistrationResponseCode start(String lobbyUUID);
	
	LobbyStatusResponse status(String lobbyUUID);
	
	MatchmakingStatusResponse matchmaking(String lobbyUUID);
	
	GeneralRequestResponse callback(String lobbyUUID, CallbackCode code);
	
	GeneralRequestResponse finish(String lobbyUUID);
	
	GeneralRequestResponse dispose(String lobbyUUID);

}
