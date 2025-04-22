package de.instinct.meta.service;

import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;

public interface UserService {
	
	ProfileData getProfile(String token);
	
	RegisterResponseCode initialize(String token);
	
	NameRegisterResponseCode registerName(String token, String name);

}
