package de.instinct.api.meta.service;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.meta.dto.Loadout;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;

public interface MetaInterface extends BaseServiceInterface {

	NameRegisterResponseCode registerName(String username);
	
	ProfileData profile(String token);
	
	RegisterResponseCode initialize(String token);

	Loadout loadout(String token);
	
}
