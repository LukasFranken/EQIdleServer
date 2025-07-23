package de.instinct.meta.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.instinct.api.core.API;
import de.instinct.api.meta.dto.CommanderData;
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
	private Map<String, CommanderData> commanders;
	
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
		UserData newUser = UserData.builder()
				.profile(ProfileData.builder()
						.rank(PlayerRank.RECRUIT)
						.userRank(UserRank.REGISTERED)
						.build())
				.resources(ResourceData.builder()
						.resources(new ArrayList<>())
						.build())
				.build();
		moduleService.init(token);
		users.put(token, newUser);
		commanders.put(token, CommanderData.builder()
				.startCommandPoints(3)
				.maxCommandPoints(10)
				.commandPointsGenerationSpeed(0.1f)
				.build());
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
		LoadoutData loadout = LoadoutData.builder()
				.ships(API.shipyard().data(token).getShips()
						.stream()
						.filter(ship -> ship.isInUse())
						.toList())
				.infrastructure(API.construction().data(token))
				.commander(commanders.get(token))
				.build();
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
			if (!found) resources.getResources().add(ResourceAmount.builder()
					.type(update.getType())
					.amount(update.getAmount())
					.build());
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
			CommanderData commander = commanders.get(token);
			commander.setMaxCommandPoints(commander.getMaxCommandPoints() + 1);
			commander.setStartCommandPoints(commander.getStartCommandPoints() + 1);
			commander.setCommandPointsGenerationSpeed(commander.getCommandPointsGenerationSpeed() + 0.05f);
			grantNewRankPriviledges(token, user);
		}
		return ExperienceUpdateResponseCode.SUCCESS;
	}

	private void grantNewRankPriviledges(String token, UserData user) {
		moduleService.manageRankUp(token, user.getProfile().getRank());
	}

}
