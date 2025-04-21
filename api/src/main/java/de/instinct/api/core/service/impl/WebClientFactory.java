package de.instinct.api.core.service.impl;

import org.springframework.web.reactive.function.client.WebClient;

public class WebClientFactory {

	public static WebClient build(String baseUrl) {
		return WebClient.builder()
				.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1 * 1024 * 1024))
				.baseUrl(baseUrl)
			    .build();
	}

}
