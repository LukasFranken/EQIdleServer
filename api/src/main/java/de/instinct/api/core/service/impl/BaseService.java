package de.instinct.api.core.service.impl;

import org.springframework.web.reactive.function.client.WebClient;

import de.instinct.api.core.API;
import de.instinct.api.core.service.BaseServiceInterface;

public class BaseService implements BaseServiceInterface {
	
	protected WebClient webClient;
	protected boolean connected;
	protected String url;
	
	public void loadURL(String tag) {
		url = API.discovery().discover(tag).getServiceUrl();
	}
	
	@Override
	public void connect() {
		try {
			webClient.get()
				.uri("/ping")
				.retrieve()
				.bodyToMono(String.class)
				.block();
		} catch (Exception e) {
			System.out.println("Error connecting to URL: " + url);
		}
	}
	
	@Override
	public boolean isConnected() {
		return false;
	}

}
