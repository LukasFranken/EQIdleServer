package de.instinct.eq_auth.service;

import de.instinct.eq_auth.controller.dto.TokenVerificationResponse;

public interface AuthenticationService {

	TokenVerificationResponse verify(String token);

	String register();

}
