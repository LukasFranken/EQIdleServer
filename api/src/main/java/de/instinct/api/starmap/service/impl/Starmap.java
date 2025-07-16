package de.instinct.api.starmap.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.CompletionResponse;
import de.instinct.api.starmap.dto.PlayerStarmapData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;
import de.instinct.api.starmap.service.StarmapInterface;

public class Starmap extends BaseService implements StarmapInterface {

	public Starmap() {
		super("starmap");
	}

	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}
	
	@Override
	public StarmapInitializationResponseCode init(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("init")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, StarmapInitializationResponseCode.class);
	}

	@Override
	public PlayerStarmapData data() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("data")
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, PlayerStarmapData.class);
	}

	@Override
	public SectorData sector() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("sector")
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, SectorData.class);
	}

	@Override
	public CompletionResponse complete(CompletionRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("complete")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, CompletionResponse.class);
	}

}
