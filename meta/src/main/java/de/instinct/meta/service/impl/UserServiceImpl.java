package de.instinct.meta.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.instinct.api.meta.dto.ExperienceUpdateResponseCode;
import de.instinct.api.meta.dto.Loadout;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.PlayerRank;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.meta.dto.ResourceUpdateResponseCode;
import de.instinct.api.meta.dto.UserRank;
import de.instinct.meta.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private Map<String, ProfileData> users;
	
	public UserServiceImpl() {
		users = new HashMap<>();
	}

	@Override
	public ProfileData getProfile(String token) {
		return users.get(token);
	}

	@Override
	public RegisterResponseCode initialize(String token) {
		if (token == null || token.contentEquals("")) return RegisterResponseCode.BAD_TOKEN;
		users.put(token, ProfileData.builder()
				.rank(PlayerRank.RECRUIT)
				.userRank(UserRank.REGISTERED)
				.resources(ResourceData.builder().build())
				.build());
		return RegisterResponseCode.SUCCESS;
	}

	@Override
	public NameRegisterResponseCode registerName(String token, String name) {
		if (!usernameExists(name)) {
			if (users.containsKey(token)) {
				users.get(token).setUsername(name);
				return NameRegisterResponseCode.SUCCESS;
			} else {
				return NameRegisterResponseCode.BAD_TOKEN;
			}
		} else {
			return NameRegisterResponseCode.USERNAME_TAKEN;
		}
	}

	private boolean usernameExists(String name) {
		for (ProfileData profile : users.values()) {
			if (profile.getUsername() != null && profile.getUsername().contentEquals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Loadout getLoadout(String token) {
		return Loadout.builder()
				.fleetMovementSpeed(50f)
				.resourceGenerationSpeed(1f)
				.commandPointsGenerationSpeed(0.1)
				.maxCommandPoints(10)
				.maxPlanetCapacity(20)
				.startCommandPoints(3)
				.build();
	}

	@Override
	public String token(String username) {
	    return users.entrySet().stream()
	                .filter(user -> username.equals(user.getValue().getUsername()))
	                .map(Map.Entry::getKey)
	                .findFirst()
	                .orElse(null);
	}

	@Override
	public ResourceUpdateResponseCode updateResources(String token, ResourceData resourceUpdate) {
		ProfileData userProfile = users.get(token);
		if (userProfile == null) return ResourceUpdateResponseCode.INVALID_TOKEN;
		if (userProfile.getResources() == null) return ResourceUpdateResponseCode.ERROR;
		updateResourceData(userProfile.getResources(), resourceUpdate);
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
		ProfileData userProfile = users.get(token);
		if (userProfile == null) return ExperienceUpdateResponseCode.INVALID_TOKEN;
		long exp = 0;
		try {
			exp = Long.parseLong(experience);
		} catch (Exception e) {
			return ExperienceUpdateResponseCode.ERROR;
		}
		userProfile.setCurrentExp(userProfile.getCurrentExp() + exp);
		if (userProfile.getRank().getNextRequiredExp() <= userProfile.getCurrentExp()) {
			userProfile.setRank(userProfile.getRank().getNextRank());
		}
		return ExperienceUpdateResponseCode.SUCCESS;
	}

}
