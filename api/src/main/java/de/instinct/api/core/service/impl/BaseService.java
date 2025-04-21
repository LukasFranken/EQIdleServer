package de.instinct.api.core.service.impl;

import org.springframework.web.reactive.function.client.WebClient;

import de.instinct.api.core.API;
import de.instinct.api.core.service.BaseServiceInterface;

public class BaseService implements BaseServiceInterface {
	
	protected WebClient webClient;
	protected boolean connected;
	protected String baseUrl;
	
	private String tag;
	
	public BaseService(String tag) {
		this.tag = tag;
	}
	
	public void loadURL() {
		baseUrl = API.discovery().discover(tag).getServiceUrl();
		if (baseUrl == null) System.out.println("Error loading baseUrl for " + tag);
	}
	
	@Override
	public void connect() {
		if (baseUrl == null ) {
			System.out.println("Can't connect: Missing baseUrl for " + tag);
			return;
		}
		webClient = WebClient.builder()
				.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1 * 1024 * 1024))
				.baseUrl(baseUrl)
			    .build();
		try {
			webClient.get()
				.uri("/ping")
				.retrieve()
				.bodyToMono(String.class)
				.block();
			connected = true;
		} catch (Exception e) {
			System.out.println("Error connecting to URL: " + baseUrl);
		}
	}
	
	@Override
	public boolean isConnected() {
		if (baseUrl == null) System.out.println("No URL loaded for " + tag);
		if (!connected) System.out.println("Not connected to URL: " + baseUrl);
		return connected;
	}

}
