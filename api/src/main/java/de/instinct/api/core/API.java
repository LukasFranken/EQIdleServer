package de.instinct.api.core;

import de.instinct.api.auth.service.AuthenticationInterface;
import de.instinct.api.auth.service.impl.Authentication;
import de.instinct.api.core.service.impl.WebClientFactory;
import de.instinct.api.discovery.service.Discovery;
import de.instinct.api.discovery.service.impl.DiscoveryInterface;

public class API {
	
	private static WebClientFactory webClientFactory;
	private static Discovery discovery;
	private static Authentication authentication;
	
	public static void initialize() {
		webClientFactory = new WebClientFactory();
		
		discovery = new Discovery();
		authentication = new Authentication();
		
	}
	
	public static DiscoveryInterface discovery() {
		if (!isInitialized()) return null;
		if (!discovery.isConnected()) return null;
		return discovery;
	}
	
	public static AuthenticationInterface authentication() {
		if (!isInitialized()) return null;
		if (!authentication.isConnected()) return null;
		return authentication;
	}

	private static boolean isInitialized() {
		if (webClientFactory == null) {
			System.out.println("API not connected. Connect first via API.initialize()!");
			return false;
		}
		return true;
	}

}
