package de.instinct.api.matchmaking.service;

import de.instinct.api.core.model.GeneralRequestResponse;
import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.FinishGameData;
import de.instinct.api.matchmaking.dto.InviteResponse;
import de.instinct.api.matchmaking.dto.InvitesStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyCreationResponse;
import de.instinct.api.matchmaking.dto.LobbyLeaveResponse;
import de.instinct.api.matchmaking.dto.LobbyStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyTypeSetResponse;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponseCode;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.api.matchmaking.dto.MatchmakingStopResponseCode;
import de.instinct.api.matchmaking.dto.PlayerReward;
import de.instinct.api.matchmaking.model.GameType;

public interface MatchmakingInterface extends BaseServiceInterface {
	
	LobbyCreationResponse create();
	
	LobbyLeaveResponse leave();
	
	String get();
	
	LobbyTypeSetResponse settype(String lobbyUUID, GameType selectedGameType);
	
	LobbyTypeSetResponse resettype(String lobbyUUID);
	
	InviteResponse invite(String username);
	
	String accept(String lobbyUUID);
	
	String decline(String lobbyUUID);
	
	InvitesStatusResponse invites();
	
	MatchmakingRegistrationResponseCode start(String lobbyUUID);
	
	LobbyStatusResponse status(String lobbyUUID);
	
	MatchmakingStatusResponse matchmaking(String lobbyUUID);
	
	MatchmakingStopResponseCode stop(String lobbyUUID);
	
	GeneralRequestResponse callback(String lobbyUUID, CallbackCode code);
	
	GeneralRequestResponse finish(String lobbyUUID, FinishGameData finishGameData);
	
	GeneralRequestResponse dispose(String lobbyUUID);
	
	PlayerReward result(String gamesessionUUID);

}
