package de.instinct.api.matchmaking.service.impl;

import de.instinct.api.core.model.GeneralRequestResponse;
import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationRequest;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponse;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
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
	public MatchmakingRegistrationResponse register(MatchmakingRegistrationRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("register")
				.payload(request)
				.build());
		return ObjectJSONMapper.mapJSON(response, MatchmakingRegistrationResponse.class);
	}

	@Override
	public MatchmakingStatusResponse status(String lobbyUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("status")
				.pathVariable(lobbyUUID)
				.build());
		return ObjectJSONMapper.mapJSON(response, MatchmakingStatusResponse.class);
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
