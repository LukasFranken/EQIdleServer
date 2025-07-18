package de.instinct.meta.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import de.instinct.api.core.modules.MenuModule;
import de.instinct.api.core.modules.ModuleUnlockRequirement;
import de.instinct.api.meta.dto.PlayerRank;
import de.instinct.api.meta.dto.modules.ModuleData;
import de.instinct.api.meta.dto.modules.ModuleInfoRequest;
import de.instinct.api.meta.dto.modules.ModuleInfoResponse;
import de.instinct.api.meta.dto.modules.ModuleRegistrationResponseCode;
import de.instinct.meta.service.ModuleService;

@Service
public class ModuleServiceImpl implements ModuleService {
	
	private Map<String, ModuleData> moduleDatas;
	private Map<MenuModule, PlayerRank> unlockRanks;
	
	public ModuleServiceImpl() {
		moduleDatas = new HashMap<>();
		unlockRanks = new HashMap<>();
		unlockRanks.put(MenuModule.PLAY, PlayerRank.RECRUIT);
		unlockRanks.put(MenuModule.SETTINGS, PlayerRank.RECRUIT);
		unlockRanks.put(MenuModule.PROFILE, PlayerRank.RECRUIT);
		unlockRanks.put(MenuModule.STARMAP, PlayerRank.RECRUIT);
		
		unlockRanks.put(MenuModule.INVENTORY, PlayerRank.PRIVATE);
		unlockRanks.put(MenuModule.SHIPYARD, PlayerRank.PRIVATE);
		unlockRanks.put(MenuModule.CONSTRUCTION, PlayerRank.PRIVATE);
		
		unlockRanks.put(MenuModule.SHOP, PlayerRank.SPECIALIST1);
		unlockRanks.put(MenuModule.MARKET, PlayerRank.CAPTAIN1);
	}
	
	@Override
	public void init(String token) {
		ModuleData newModuleData = ModuleData.builder()
				.enabledModules(new ArrayList<>())
				.build();
		for (MenuModule module : MenuModule.values()) {
			if (unlockRanks.get(module) == null) continue;
			if (unlockRanks.get(module) == PlayerRank.RECRUIT) newModuleData.getEnabledModules().add(module);
		}
		moduleDatas.put(token, newModuleData);
	}
	
	@Override
	public ModuleData getModules(String token) {
		ModuleData moduleData = moduleDatas.get(token);
		if (moduleData == null) return null;
		return moduleData;
	}
	
	@Override
	public ModuleRegistrationResponseCode registerModule(String token, MenuModule module) {
		ModuleData moduleData = moduleDatas.get(token);
		if (moduleData == null) return ModuleRegistrationResponseCode.INVALID_TOKEN;
		if (moduleData.getEnabledModules().contains(module)) return ModuleRegistrationResponseCode.ALREADY_REGISTERED;
		moduleData.getEnabledModules().add(module);
		return ModuleRegistrationResponseCode.SUCCESS;
	}

	@Override
	public void manageRankUp(String token, PlayerRank rank) {
		ModuleData moduleData = getModules(token);
		if (moduleData == null) return;
		for (Entry<MenuModule, PlayerRank> entry : unlockRanks.entrySet()) {
			if (rank.ordinal() >= entry.getValue().ordinal() && !moduleData.getEnabledModules().contains(entry.getKey())) {
				moduleData.getEnabledModules().add(entry.getKey());
			}
		}
	}

	@Override
	public ModuleInfoResponse getInfo(ModuleInfoRequest moduleInfoRequest) {
		List<ModuleUnlockRequirement> requestedUnlockRequirements = new ArrayList<>();
		for (MenuModule menuModule : moduleInfoRequest.getRequestedModuleInfos()) {
			requestedUnlockRequirements.add(ModuleUnlockRequirement.builder()
					.module(menuModule)
					.requiredRank(unlockRanks.get(menuModule))
					.build());
		}
		return ModuleInfoResponse.builder()
				.unlockRequirements(requestedUnlockRequirements)
				.build();
	}

}
