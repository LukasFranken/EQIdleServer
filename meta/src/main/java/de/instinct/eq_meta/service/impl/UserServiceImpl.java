package de.instinct.eq_meta.service.impl;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.instinct.eq_meta.controller.dto.NameRegisterResponseCode;
import de.instinct.eq_meta.controller.dto.RegisterResponseCode;
import de.instinct.eq_meta.service.UserService;
import de.instinct.eq_meta.service.model.PlayerRank;
import de.instinct.eq_meta.service.model.ProfileData;
import de.instinct.eq_meta.service.model.TokenVerificationResponse;
import de.instinct.eq_meta.service.model.UserRank;

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
	public RegisterResponseCode register(String token) {
		TokenVerificationResponse response = authServiceClient.get()
				.uri("/verify/" + token)
				.retrieve()
				.bodyToMono(TokenVerificationResponse.class)
				.timeout(Duration.ofMillis(2000))
				.block();
		if (response == TokenVerificationResponse.VERIFIED) {
			users.put(token, ProfileData.builder()
					.rank(PlayerRank.RECRUIT)
					.userRank(UserRank.REGISTERED)
					.build());
			return RegisterResponseCode.SUCCESS;
		}
		return RegisterResponseCode.BAD_TOKEN;
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
