package de.instinct.api.core;

import de.instinct.api.auth.service.AuthenticationInterface;
import de.instinct.api.auth.service.impl.Authentication;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.api.discovery.service.DiscoveryInterface;
import de.instinct.api.discovery.service.impl.Discovery;
import de.instinct.api.matchmaking.service.MatchmakingInterface;
import de.instinct.api.matchmaking.service.impl.Matchmaking;
import de.instinct.api.meta.service.MetaInterface;
import de.instinct.api.meta.service.impl.Meta;

public class API {
	
	public static APIConfiguration configuration;
	public static String authKey;
	
	private static Discovery discovery;
	private static Authentication authentication;
	private static Meta meta;
	private static Matchmaking matchmaking;
	
	public static void initialize(APIConfiguration newConfiguration) {
		configuration = newConfiguration;
		discovery = new Discovery();
		authentication = new Authentication();
		meta = new Meta();
		matchmaking = new Matchmaking();
		
		if (newConfiguration != APIConfiguration.SERVER) {
			discovery().connect();
			authentication().connect();
			meta().connect();
			matchmaking().connect();
			printAPIStatus();
		}
	}
	
	public static DiscoveryInterface discovery() {
		if (!isInitialized()) return null;
		return discovery;
	}
	
	public static AuthenticationInterface authentication() {
		if (!isInitialized()) return null;
		return authentication;
	}
	
	public static MetaInterface meta() {
		if (!isInitialized()) return null;
		return meta;
	}
	
	public static MatchmakingInterface matchmaking() {
		if (!isInitialized()) return null;
		return matchmaking;
	}
	
	public static void printAPIStatus() {
		System.out.println("---------API STATUS---------");
		System.out.println("Initialized: " + isInitialized());
		System.out.println("----------SERVICES----------");
		System.out.println("Discovery: " + discovery.isConnected());
		System.out.println("----------------------------");
		System.out.println("Authentication: " + authentication.isConnected());
		System.out.println("----------------------------");
		System.out.println("Meta: " + meta.isConnected());
		System.out.println("----------------------------");
		System.out.println("Matchmaking: " + matchmaking.isConnected());
		System.out.println("----------------------------");
	}

	private static boolean isInitialized() {
		if (discovery == null) {
			System.out.println("API not connected. Connect first via API.initialize()!");
			return false;
		}
		return true;
	}

}
