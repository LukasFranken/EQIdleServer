package de.instinct.api.core;

import de.instinct.api.core.service.impl.WebClientFactory;
import de.instinct.api.discovery.service.Discovery;
import de.instinct.api.discovery.service.impl.DiscoveryInterface;

public class API {
	
	private static WebClientFactory webClientFactory;
	private static Discovery discovery;
	
	public static void initialize() {
		webClientFactory = new WebClientFactory();
		
		discovery = new Discovery();
		
	}
	
	public static DiscoveryInterface discovery() {
		if (!isInitialized()) return null;
		if (!discovery.isConnected()) return null;
		return discovery;
	}

	private static boolean isInitialized() {
		if (webClientFactory == null) {
			System.out.println("API not connected. Connect first via API.initialize()!");
			return false;
		}
		return true;
	}

}
