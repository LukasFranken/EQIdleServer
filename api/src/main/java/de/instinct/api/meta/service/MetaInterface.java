package de.instinct.api.meta.service;

import de.instinct.api.core.modules.MenuModule;
import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.meta.dto.ExperienceUpdateResponseCode;
import de.instinct.api.meta.dto.LoadoutData;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.meta.dto.ResourceUpdateResponseCode;
import de.instinct.api.meta.dto.modules.ModuleData;
import de.instinct.api.meta.dto.modules.ModuleInfoRequest;
import de.instinct.api.meta.dto.modules.ModuleInfoResponse;
import de.instinct.api.meta.dto.modules.ModuleRegistrationResponseCode;

public interface MetaInterface extends BaseServiceInterface {

	NameRegisterResponseCode registerName(String username);
	
	ProfileData profile(String token);
	
	ResourceData resources(String token);
	
	ModuleData modules(String token);
	
	ModuleRegistrationResponseCode registerModule(String token, MenuModule module);
	
	ModuleInfoResponse moduleInfo(ModuleInfoRequest moduleInfoRequest);
	
	RegisterResponseCode initialize(String token);

	LoadoutData loadout(String token);

	String token(String username);
	
	ResourceUpdateResponseCode addResources(String token, ResourceData resourceUpdate);
	
	ExperienceUpdateResponseCode experience(String token, long experience);
	
}
