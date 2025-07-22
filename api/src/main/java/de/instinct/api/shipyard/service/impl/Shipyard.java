package de.instinct.api.shipyard.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shipyard.dto.ShipAddResponse;
import de.instinct.api.shipyard.dto.ShipBuildResponse;
import de.instinct.api.shipyard.dto.ShipUpgradeResponse;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.StatChangeResponse;
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
	public PlayerShipyardData data(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("data")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, PlayerShipyardData.class);
	}
	
	@Override
	public ShipyardData shipyard() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("shipyard")
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

	@Override
	public StatChangeResponse hangar(String token, int count) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("hangar")
				.pathVariable(token + "/" + count)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, StatChangeResponse.class);
	}

	@Override
	public StatChangeResponse active(String token, int count) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("active")
				.pathVariable(token + "/" + count)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, StatChangeResponse.class);
	}

	@Override
	public ShipBuildResponse build(String shiptoken) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("build")
				.pathVariable(shiptoken)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipBuildResponse.class);
	}

	@Override
	public ShipUpgradeResponse upgrade(String shiptoken) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("upgrade")
				.pathVariable(shiptoken)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipUpgradeResponse.class);
	}
	
	@Override
	public ShipAddResponse add(String token, int shipid) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("add")
				.pathVariable(token + "/" + shipid)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShipAddResponse.class);
	}

}
