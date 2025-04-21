package de.instinct.api.auth.service.impl;

import de.instinct.api.auth.dto.TokenVerificationResponse;
import de.instinct.api.auth.service.AuthenticationInterface;
import de.instinct.api.core.service.impl.BaseService;

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
		return webClient.get()
				.uri("/verify/" + token)
				.retrieve()
				.bodyToMono(TokenVerificationResponse.class)
				.block();
	}

	@Override
	public String register() {
		if (!isConnected()) return null;
		return webClient.get()
				.uri("/register")
				.retrieve()
				.bodyToMono(String.class)
				.block();
	}

}
