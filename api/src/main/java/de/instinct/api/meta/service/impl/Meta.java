package de.instinct.api.meta.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.meta.dto.Loadout;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.service.MetaInterface;

public class Meta extends BaseService implements MetaInterface {

	public Meta() {
		super("meta");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}

	@Override
	public NameRegisterResponseCode registerName(String username) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("register")
				.pathVariable(username)
				.build());
		return ObjectJSONMapper.mapJSON(response, NameRegisterResponseCode.class);
	}

	@Override
	public ProfileData profile(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("profile")
				.pathVariable(token)
				.build());
		return ObjectJSONMapper.mapJSON(response, ProfileData.class);
	}

	@Override
	public RegisterResponseCode initialize(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("initialize")
				.pathVariable(token)
				.build());
		return ObjectJSONMapper.mapJSON(response, RegisterResponseCode.class);
	}

	@Override
	public Loadout loadout(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("loadout")
				.pathVariable(token)
				.build());
		return ObjectJSONMapper.mapJSON(response, Loadout.class);
	}

	@Override
	public String token(String username) {
		if (!isConnected()) return null;
		return super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("token")
				.pathVariable(username)
				.build());
	}

}
