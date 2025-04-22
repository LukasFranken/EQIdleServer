package de.instinct.api.auth.service.impl;

import de.instinct.api.auth.dto.TokenVerificationResponse;
import de.instinct.api.auth.service.AuthenticationInterface;
import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;

public class Authentication extends BaseService implements AuthenticationInterface {
	
	public Authentication() {
		super("auth");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}

	@Override
	public TokenVerificationResponse verify(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("verify")
				.pathVariable(token)
				.build());
		return ObjectJSONMapper.mapJSON(response, TokenVerificationResponse.class);
	}

	@Override
	public String register() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("register")
				.build());
		return response;
	}

}
