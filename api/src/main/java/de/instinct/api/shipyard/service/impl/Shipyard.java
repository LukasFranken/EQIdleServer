package de.instinct.api.shipyard.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;
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

	@Override
	public UseShipResponseCode use(String token, String shipUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("use")
				.pathVariable(token + "/" + shipUUID)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, UseShipResponseCode.class);
	}

	@Override
	public UnuseShipResponseCode unuse(String token, String shipUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("unuse")
				.pathVariable(token + "/" + shipUUID)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, UnuseShipResponseCode.class);
	}

}
