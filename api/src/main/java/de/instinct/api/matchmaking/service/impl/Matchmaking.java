package de.instinct.api.matchmaking.service.impl;

import de.instinct.api.core.model.GeneralRequestResponse;
import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
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
import de.instinct.api.matchmaking.service.MatchmakingInterface;

public class Matchmaking extends BaseService implements MatchmakingInterface {

	public Matchmaking() {
		super("matchmaking");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}

	@Override
	public LobbyCreationResponse create() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("create")
				.build());
		return ObjectJSONMapper.mapJSON(response, LobbyCreationResponse.class);
	}
	
	@Override
	public LobbyLeaveResponse leave() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("leave")
				.build());
		return ObjectJSONMapper.mapJSON(response, LobbyLeaveResponse.class);
	}
	
	@Override
	public String get() {
		if (!isConnected()) return null;
		return super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("get")
				.build());
	}

	@Override
	public LobbyTypeSetResponse settype(String lobbyUUID, GameType selectedGameType) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("settype")
				.pathVariable(lobbyUUID)
				.payload(selectedGameType)
				.build());
		return ObjectJSONMapper.mapJSON(response, LobbyTypeSetResponse.class);
	}
	
	@Override
	public LobbyTypeSetResponse resettype(String lobbyUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("resettype")
				.pathVariable(lobbyUUID)
				.build());
		return ObjectJSONMapper.mapJSON(response, LobbyTypeSetResponse.class);
	}

	@Override
	public InviteResponse invite(String username) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("invite")
				.pathVariable(username)
				.build());
		return ObjectJSONMapper.mapJSON(response, InviteResponse.class);
	}

	@Override
	public String accept(String lobbyUUID) {
		if (!isConnected()) return null;
		return super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("accept")
				.pathVariable(lobbyUUID)
				.build());
	}

	@Override
	public String decline(String lobbyUUID) {
		if (!isConnected()) return null;
		return super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("decline")
				.pathVariable(lobbyUUID)
				.build());
	}

	@Override
	public InvitesStatusResponse invites() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("invites")
				.build());
		return ObjectJSONMapper.mapJSON(response, InvitesStatusResponse.class);
	}

	@Override
	public MatchmakingRegistrationResponseCode start(String lobbyUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("start")
				.pathVariable(lobbyUUID)
				.build());
		return ObjectJSONMapper.mapJSON(response, MatchmakingRegistrationResponseCode.class);
	}

	@Override
	public LobbyStatusResponse status(String lobbyUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("status")
				.pathVariable(lobbyUUID)
				.build());
		return ObjectJSONMapper.mapJSON(response, LobbyStatusResponse.class);
	}

	@Override
	public MatchmakingStatusResponse matchmaking(String lobbyUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("matchmaking")
				.pathVariable(lobbyUUID)
				.build());
		return ObjectJSONMapper.mapJSON(response, MatchmakingStatusResponse.class);
	}
	
	@Override
	public MatchmakingStopResponseCode stop(String lobbyUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("stop")
				.pathVariable(lobbyUUID)
				.build());
		return ObjectJSONMapper.mapJSON(response, MatchmakingStopResponseCode.class);
	}

	@Override
	public GeneralRequestResponse finish(String lobbyUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.PUT)
				.endpoint("finish")
				.pathVariable(lobbyUUID)
				.build());
		return ObjectJSONMapper.mapJSON(response, GeneralRequestResponse.class);
	}
	
	@Override
	public GeneralRequestResponse callback(String lobbyUUID, CallbackCode code) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.PUT)
				.endpoint("callback")
				.pathVariable(lobbyUUID)
				.payload(code)
				.build());
		return ObjectJSONMapper.mapJSON(response, GeneralRequestResponse.class);
	}

	@Override
	public GeneralRequestResponse dispose(String lobbyUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.PUT)
				.endpoint("dispose")
				.pathVariable(lobbyUUID)
				.build());
		return ObjectJSONMapper.mapJSON(response, GeneralRequestResponse.class);
	}

}
