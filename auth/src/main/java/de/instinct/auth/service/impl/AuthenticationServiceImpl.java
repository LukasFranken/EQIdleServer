package de.instinct.auth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.api.auth.dto.TokenVerificationResponse;
import de.instinct.api.core.API;
import de.instinct.auth.service.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private List<String> tokens;
	
	public AuthenticationServiceImpl() {
		tokens = new ArrayList<>();
		tokens.add("testuuid");
	}

	@Override
	public TokenVerificationResponse verify(String token) {
		if (tokens.contains(token)) {
			return TokenVerificationResponse.VERIFIED;
		} else {
			return TokenVerificationResponse.DOESNT_EXIST;
		}
	}

	@Override
	public String register() {
		String newToken = UUID.randomUUID().toString();
		tokens.add(newToken);
		registerMeta(newToken);
		return newToken;
	}

	private void registerMeta(String newToken) {
		API.meta().initialize(newToken);
	}

}
