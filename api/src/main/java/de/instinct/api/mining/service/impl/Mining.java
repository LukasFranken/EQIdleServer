package de.instinct.api.mining.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;
import de.instinct.api.mining.dto.Maps;
import de.instinct.api.mining.dto.player.MiningPlayerMissionData;
import de.instinct.api.mining.service.MiningInterface;

public class Mining extends BaseService implements MiningInterface {

	public Mining() {
		super("mining");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}
	
	@Override
	public void start() {
		if (!isConnected()) return;
		super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("start")
				.build());
	}

	@Override
	public CreateSessionResponse createSession(CreateSessionRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("create")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, CreateSessionResponse.class);
	}

	@Override
	public MiningPlayerMissionData missiondata(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("missiondata")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, MiningPlayerMissionData.class);
	}

	@Override
	public Maps maps() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("maps")
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, Maps.class);
	}

}
