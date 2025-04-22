package de.instinct.api.core.service.impl;

import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import de.instinct.api.core.API;
import de.instinct.api.core.model.HeaderValue;
import de.instinct.api.core.model.RESTRequest;
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
		if (baseUrl == null) {
			System.out.println("Can't connect: Missing baseUrl for " + tag);
			return;
		}
		if (connected) {
			System.out.println("Already connected: " + tag);
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
	public void disconnect() {
		webClient = null;
		connected = false;
	}
	
	@Override
	public boolean isConnected() {
		if (baseUrl == null) System.out.println("No URL loaded for " + tag);
		if (!connected) System.out.println("Not connected to URL: " + baseUrl);
		return connected;
	}
	
	protected String sendRequest(RESTRequest request) {
		setAuthToken(request);
		return switch (request.getType()) {
	    	case GET -> sendGetRequest(request);
	    	case POST -> sendPostRequest(request);
	    	case PUT -> sendPutRequest(request);
	    	case DELETE -> sendDeleteRequest(request);
		};
	}
	
	private void setAuthToken(RESTRequest request) {
		if (request.getRequestHeader() == null) {
			request.setRequestHeader(new ArrayList<>());
		}
		request.getRequestHeader().add(HeaderValue.builder()
				.key("token")
				.value(API.authKey == null ? "" : API.authKey)
				.build());
	}

	private String sendGetRequest(RESTRequest request) {
		return webClient.get()
			.uri(buildURI(request))
			.headers(headers -> applyHeaders(headers, request))
			.retrieve()
			.bodyToMono(String.class)
			.block();
	}
	
	private String sendPostRequest(RESTRequest request) {
		return webClient.post()
				.uri(buildURI(request))
				.headers(headers -> applyHeaders(headers, request))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(request.getPayload() == null ? "" : request.getPayload())
				.retrieve()
				.bodyToMono(String.class)
				.block();
	}
	
	private String sendPutRequest(RESTRequest request) {
		return null;
	}
	
	private String sendDeleteRequest(RESTRequest request) {
		return null;
	}
	
	private String buildURI(RESTRequest request) {
		String endpoint = request.getEndpoint() == null ? "" : "/" + request.getEndpoint();
		String param = request.getPathVariable() == null ? "" : "/" + request.getPathVariable();
		return endpoint + param;
	}
	
	private void applyHeaders(HttpHeaders headers, RESTRequest request) {
	    if (request.getRequestHeader() != null) {
	        request.getRequestHeader().forEach(header ->
	            headers.add(header.getKey(), header.getValue())
	        );
	    }
	}

}
