package de.instinct.api.construction.service.impl;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;
import de.instinct.api.construction.dto.UseTurretResponseCode;
import de.instinct.api.construction.service.ConstructionInterface;
import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;

public class Construction extends BaseService implements ConstructionInterface {
	
	public Construction() {
		super("construction");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}
	
	@Override
	public InfrastructureInitializationResponseCode init(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("init")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, InfrastructureInitializationResponseCode.class);
	}
	
	@Override
	public Infrastructure data(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("data")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, Infrastructure.class);
	}
	
	@Override
	public UseTurretResponseCode use(String token, String turretUUID) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("use")
				.pathVariable(token + "/" + turretUUID)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, UseTurretResponseCode.class);
	}

}
