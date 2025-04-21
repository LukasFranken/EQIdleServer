package de.instinct.api.auth.service;

import de.instinct.api.auth.dto.TokenVerificationResponse;
import de.instinct.api.core.service.BaseServiceInterface;

public interface AuthenticationInterface extends BaseServiceInterface {
	
	TokenVerificationResponse verify(String token);
	
	String register();

}
