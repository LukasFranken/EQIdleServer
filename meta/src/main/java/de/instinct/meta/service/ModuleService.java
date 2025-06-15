package de.instinct.meta.service;

import de.instinct.api.core.modules.MenuModule;
import de.instinct.api.meta.dto.PlayerRank;
import de.instinct.api.meta.dto.modules.ModuleData;
import de.instinct.api.meta.dto.modules.ModuleInfoRequest;
import de.instinct.api.meta.dto.modules.ModuleInfoResponse;
import de.instinct.api.meta.dto.modules.ModuleRegistrationResponseCode;

public interface ModuleService {
	
	void init(String token);
	
	ModuleData getModules(String token);

	ModuleRegistrationResponseCode registerModule(String token, MenuModule module);

	void manageRankUp(String token, PlayerRank rank);

	ModuleInfoResponse getInfo(ModuleInfoRequest moduleInfoRequest);

}
