package de.instinct.meta.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.instinct.api.commander.dto.CommanderData;
import de.instinct.api.core.API;
import de.instinct.api.meta.dto.ExperienceUpdateResponseCode;
import de.instinct.api.meta.dto.LoadoutData;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.PlayerRank;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.dto.ResourceAmount;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.meta.dto.ResourceUpdateResponseCode;
import de.instinct.api.meta.dto.UserRank;
import de.instinct.meta.service.ModuleService;
import de.instinct.meta.service.UserService;
import de.instinct.meta.service.model.UserData;

@Service
public class UserServiceImpl implements UserService {
	
	private Map<String, UserData> users;
	
	@Autowired
	private ModuleService moduleService;
	
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
	public ResourceData getResources(String token) {
		UserData user = users.get(token);
		if (user == null) return null;
		return user.getResources();
	}

	@Override
	public RegisterResponseCode initialize(String token) {
		if (token == null || token.contentEquals("")) return RegisterResponseCode.BAD_TOKEN;
		ProfileData newProfile = new ProfileData();
		newProfile.setRank(PlayerRank.RECRUIT);
		newProfile.setUserRank(UserRank.REGISTERED);
		ResourceData newResources = new ResourceData();
		newResources.setResources(new ArrayList<>());
		UserData newUser = UserData.builder()
				.profile(newProfile)
				.resources(newResources)
				.build();
		moduleService.init(token);
		users.put(token, newUser);
		API.commander().init(token);
		API.shipyard().init(token);
		API.construction().init(token);
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
		LoadoutData loadout = new LoadoutData();
		loadout.setShips(API.shipyard().data(token).getShips()
				.stream()
				.filter(ship -> ship.isInUse())
				.toList());
		loadout.setInfrastructure(API.construction().data(token));
		loadout.setCommander(API.commander().data(token));
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
		for (ResourceAmount update : resourceUpdate.getResources()) {
			boolean found = false;
			for (ResourceAmount resource : resources.getResources()) {
				if (resource.getType() == update.getType()) {
					resource.setAmount(resource.getAmount() + update.getAmount());
					found = true;
					break;
				}
			}
			
			if (!found) {
				ResourceAmount resourceAmount = new ResourceAmount();
				resourceAmount.setType(update.getType());
				resourceAmount.setAmount(update.getAmount());
				resources.getResources().add(resourceAmount);
			}
		}
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
		while (profile.getRank().getNextRequiredExp() <= profile.getCurrentExp()) {
			profile.setRank(profile.getRank().getNextRank());
			API.commander().rankup(token, profile.getRank());
			moduleService.manageRankUp(token, profile.getRank());
		}
		return ExperienceUpdateResponseCode.SUCCESS;
	}

}
