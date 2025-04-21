package de.instinct.eq_auth.service;

import de.instinct.api.auth.dto.TokenVerificationResponse;

public interface AuthenticationService {

	TokenVerificationResponse verify(String token);

	String register();

}
