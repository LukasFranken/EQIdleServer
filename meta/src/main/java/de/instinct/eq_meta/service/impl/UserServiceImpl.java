package de.instinct.eq_meta.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.PlayerRank;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.dto.UserRank;
import de.instinct.eq_meta.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private Map<String, ProfileData> users;
	
	private WebClient authServiceClient;
	
	public UserServiceImpl() {
		users = new HashMap<>();
		authServiceClient = WebClient.builder()
				.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1 * 1024 * 1024))
				.baseUrl("http://localhost:9009/auth")
			    .build();
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

}
