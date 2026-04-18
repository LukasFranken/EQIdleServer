package de.instinct.api.auth.service;

import de.instinct.api.auth.dto.TokenVerificationResponse;

public interface AuthenticationInterface {
	
	TokenVerificationResponse verify(String token);
	
	String register();

}
