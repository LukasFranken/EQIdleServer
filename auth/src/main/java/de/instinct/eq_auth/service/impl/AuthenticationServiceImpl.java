package de.instinct.eq_auth.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import de.instinct.eq_auth.controller.dto.RegisterResponseCode;
import de.instinct.eq_auth.controller.dto.TokenVerificationResponse;
import de.instinct.eq_auth.service.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private List<String> tokens;
	
	private WebClient metaServiceClient;
	
	public AuthenticationServiceImpl() {
		tokens = new ArrayList<>();
		tokens.add("testuuid");
		
		metaServiceClient = WebClient.builder()
				.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1 * 1024 * 1024))
				.baseUrl("http://localhost:9008/meta")
			    .build();
	}

	@Override
	public TokenVerificationResponse verify(String token) {
		if (tokens.contains(token)) {
			return TokenVerificationResponse.VERIFIED;
		} else {
			return TokenVerificationResponse.DOESNT_EXIST;
		}
	}

	@Override
	public String register() {
		String newToken = UUID.randomUUID().toString();
		tokens.add(newToken);
		registerMeta(newToken);
		return newToken;
	}

	private void registerMeta(String newToken) {
		metaServiceClient.post()
			.uri("/register")
			.header("token", newToken)
			.retrieve()
			.bodyToMono(RegisterResponseCode.class)
			.timeout(Duration.ofMillis(2000))
			.block();
	}

}
