package de.instinct.api.shipyard.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.service.ShipyardInterface;

public class Shipyard extends BaseService implements ShipyardInterface {

	public Shipyard() {
		super("shipyard");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}
	
	@Override
	public ShipyardInitializationResponseCode init(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("init")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipyardInitializationResponseCode.class);
	}
	
	@Override
	public ShipyardData data(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("data")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipyardData.class);
	}

}
