package de.instinct.eq_meta.service;

import de.instinct.eq_meta.controller.dto.NameRegisterResponseCode;
import de.instinct.eq_meta.controller.dto.RegisterResponseCode;
import de.instinct.eq_meta.service.model.ProfileData;

public interface UserService {
	
	ProfileData getProfile(String token);
	
	RegisterResponseCode register(String token);
	
	NameRegisterResponseCode registerName(String token, String name);

}
