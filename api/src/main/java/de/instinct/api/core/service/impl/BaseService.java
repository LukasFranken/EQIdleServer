package de.instinct.api.core.service.impl;

import org.springframework.web.reactive.function.client.WebClient;

import de.instinct.api.core.API;
import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.discovery.dto.ServiceInfoDTO;

public class BaseService implements BaseServiceInterface {
	
	protected WebClient webClient;
	protected boolean connected;
	protected String baseUrl;
	
	private String tag;
	
	public BaseService(String tag) {
		this.tag = tag;
	}
	
	public void loadURL() {
		ServiceInfoDTO serviceInfo = API.discovery().discover(tag);
		if (serviceInfo == null) {
			System.out.println("Error loading baseUrl for " + tag);
			return;
		}
		baseUrl = URLBuilder.build(serviceInfo);
	}
	
	@Override
	public void connect() {
		if (baseUrl == null ) {
			System.out.println("Can't connect: Missing baseUrl for " + tag);
			return;
		}
		webClient = WebClientFactory.build(baseUrl);
		try {
			webClient.get()
				.uri("/ping")
				.retrieve()
				.bodyToMono(String.class)
				.block();
			connected = true;
			System.out.println("Connected to " + tag + " via URL: " + baseUrl);
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
