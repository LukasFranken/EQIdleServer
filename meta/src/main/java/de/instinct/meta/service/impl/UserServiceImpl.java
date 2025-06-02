package de.instinct.meta.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import de.instinct.api.core.API;
import de.instinct.api.core.modules.MenuModule;
import de.instinct.api.meta.dto.ExperienceUpdateResponseCode;
import de.instinct.api.meta.dto.LoadoutData;
import de.instinct.api.meta.dto.ModuleData;
import de.instinct.api.meta.dto.ModuleRegistrationResponseCode;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.PlayerRank;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.meta.dto.ResourceUpdateResponseCode;
import de.instinct.api.meta.dto.ShipData;
import de.instinct.api.meta.dto.ShipType;
import de.instinct.api.meta.dto.UserRank;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.meta.service.UserService;
import de.instinct.meta.service.model.UserData;

@Service
public class UserServiceImpl implements UserService {
	
	private Map<String, UserData> users;
	
	public UserServiceImpl() {
		users = new HashMap<>();
	}

	@Override
	public ProfileData getProfile(String token) {
		UserData user = users.get(token);
		if (user == null) return null;
		return user.getProfile();
	}
	
	@Override
	public ModuleData getModules(String token) {
		UserData user = users.get(token);
		if (user == null) return null;
		return user.getModules();
	}
	
	@Override
	public ResourceData getResources(String token) {
		UserData user = users.get(token);
		if (user == null) return null;
		return user.getResources();
	}
	
	@Override
	public ModuleRegistrationResponseCode registerModule(String token, MenuModule module) {
		UserData user = users.get(token);
		if (user == null) return ModuleRegistrationResponseCode.INVALID_TOKEN;
		if (user.getModules().getEnabledModules().contains(module)) return ModuleRegistrationResponseCode.ALREADY_REGISTERED;
		user.getModules().getEnabledModules().add(module);
		return ModuleRegistrationResponseCode.SUCCESS;
	}

	@Override
	public RegisterResponseCode initialize(String token) {
		if (token == null || token.contentEquals("")) return RegisterResponseCode.BAD_TOKEN;
		UserData newUser = UserData.builder()
				.profile(ProfileData.builder()
						.rank(PlayerRank.RECRUIT)
						.userRank(UserRank.REGISTERED)
						.build())
				.modules(ModuleData.builder()
						.enabledModules(new ArrayList<>())
						.build())
				.resources(ResourceData.builder().build())
				.build();
		newUser.getModules().getEnabledModules().add(MenuModule.PLAY);
		newUser.getModules().getEnabledModules().add(MenuModule.PROFILE);
		newUser.getModules().getEnabledModules().add(MenuModule.SETTINGS);
		users.put(token, newUser);
		API.shipyard().init(token);
		return RegisterResponseCode.SUCCESS;
	}

	@Override
	public NameRegisterResponseCode registerName(String token, String name) {
		if (!usernameExists(name)) {
			if (users.containsKey(token)) {
				users.get(token).getProfile().setUsername(name);
				return NameRegisterResponseCode.SUCCESS;
			} else {
				return NameRegisterResponseCode.BAD_TOKEN;
			}
		} else {
			return NameRegisterResponseCode.USERNAME_TAKEN;
		}
	}

	private boolean usernameExists(String name) {
		for (UserData user : users.values()) {
			if (user.getProfile().getUsername() != null && user.getProfile().getUsername().contentEquals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public LoadoutData getLoadout(String token) {
		LoadoutData loadout = LoadoutData.builder()
				.ships(new ArrayList<>())
				.resourceGenerationSpeed(0.5f)
				.commandPointsGenerationSpeed(0.1)
				.maxCommandPoints(10)
				.maxPlanetCapacity(10)
				.startCommandPoints(3)
				.build();
		loadout.setShips(API.shipyard().data(token).getOwnedShips()
				.stream()
				.filter(ship -> ship.isInUse())
				.toList());
		return loadout;
	}

	@Override
	public String token(String username) {
	    return users.entrySet().stream()
	                .filter(user -> username.equals(user.getValue().getProfile().getUsername()))
	                .map(Map.Entry::getKey)
	                .findFirst()
	                .orElse(null);
	}

	@Override
	public ResourceUpdateResponseCode updateResources(String token, ResourceData resourceUpdate) {
		UserData user = users.get(token);
		if (user == null) return ResourceUpdateResponseCode.INVALID_TOKEN;
		if (user.getResources() == null) return ResourceUpdateResponseCode.ERROR;
		updateResourceData(user.getResources(), resourceUpdate);
		return ResourceUpdateResponseCode.SUCCESS;
	}
	
	public void updateResourceData(ResourceData resources, ResourceData resourceUpdate) {
		resources.setCredits(resources.getCredits() + resourceUpdate.getCredits());
		resources.setIron(resources.getIron() + resourceUpdate.getIron());
		resources.setGold(resources.getGold() + resourceUpdate.getGold());
		resources.setQuartz(resources.getQuartz() + resourceUpdate.getQuartz());
		resources.setDeuterium(resources.getDeuterium() + resourceUpdate.getDeuterium());
		resources.setEquilibrium(resources.getEquilibrium() + resourceUpdate.getEquilibrium());
	}

	@Override
	public ExperienceUpdateResponseCode addExperience(String token, String experience) {
		UserData user = users.get(token);
		if (user == null) return ExperienceUpdateResponseCode.INVALID_TOKEN;
		ProfileData profile = user.getProfile();
		long exp = 0;
		try {
			exp = Long.parseLong(experience);
		} catch (Exception e) {
			return ExperienceUpdateResponseCode.ERROR;
		}
		profile.setCurrentExp(profile.getCurrentExp() + exp);
		if (profile.getRank().getNextRequiredExp() <= profile.getCurrentExp()) {
			profile.setRank(profile.getRank().getNextRank());
			grantNewRankPriviledges(token, user);
		}
		return ExperienceUpdateResponseCode.SUCCESS;
	}

	private void grantNewRankPriviledges(String token, UserData user) {
		switch (user.getProfile().getRank()) {
		case PRIVATE:
			user.getModules().getEnabledModules().add(MenuModule.INVENTORY);
			user.getModules().getEnabledModules().add(MenuModule.SHIPYARD);
			break;
		}
	}

}
