package de.instinct.meta.service;

import de.instinct.api.core.modules.MenuModule;
import de.instinct.api.meta.dto.ExperienceUpdateResponseCode;
import de.instinct.api.meta.dto.Loadout;
import de.instinct.api.meta.dto.ModuleData;
import de.instinct.api.meta.dto.ModuleRegistrationResponseCode;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.meta.dto.ResourceUpdateResponseCode;

public interface UserService {
	
	ProfileData getProfile(String token);
	
	ModuleData getModules(String token);
	
	ResourceData getResources(String token);
	
	RegisterResponseCode initialize(String token);
	
	NameRegisterResponseCode registerName(String token, String name);

	Loadout getLoadout(String token);

	String token(String username);

	ResourceUpdateResponseCode updateResources(String token, ResourceData resourceUpdate);

	ExperienceUpdateResponseCode addExperience(String token, String experience);

	ModuleRegistrationResponseCode registerModule(String token, MenuModule module);

}
