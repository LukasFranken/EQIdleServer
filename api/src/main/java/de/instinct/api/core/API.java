package de.instinct.api.core;

import de.instinct.api.auth.service.AuthenticationInterface;
import de.instinct.api.auth.service.impl.Authentication;
import de.instinct.api.construction.service.ConstructionInterface;
import de.instinct.api.construction.service.impl.Construction;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.api.discovery.service.DiscoveryInterface;
import de.instinct.api.discovery.service.impl.Discovery;
import de.instinct.api.game.service.GameInterface;
import de.instinct.api.game.service.impl.Game;
import de.instinct.api.matchmaking.service.MatchmakingInterface;
import de.instinct.api.matchmaking.service.impl.Matchmaking;
import de.instinct.api.meta.service.MetaInterface;
import de.instinct.api.meta.service.impl.Meta;
import de.instinct.api.shipyard.service.ShipyardInterface;
import de.instinct.api.shipyard.service.impl.Shipyard;

public class API {
	
	public static APIConfiguration configuration;
	public static String authKey;
	
	private static Discovery discovery;
	private static Authentication authentication;
	private static Meta meta;
	private static Matchmaking matchmaking;
	private static Game game;
	private static Shipyard shipyard;
	private static Construction construction;
	
	public static void initialize(APIConfiguration newConfiguration) {
		configuration = newConfiguration;
		discovery = new Discovery();
		authentication = new Authentication();
		meta = new Meta();
		matchmaking = new Matchmaking();
		game = new Game();
		shipyard = new Shipyard();
		construction = new Construction();
		
		if (newConfiguration != APIConfiguration.SERVER) {
			discovery().connect();
			authentication().connect();
			meta().connect();
			matchmaking().connect();
			game().connect();
			shipyard().connect();
			construction().connect();
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
	
	public static GameInterface game() {
		if (!isInitialized()) return null;
		return game;
	}
	
	public static ShipyardInterface shipyard() {
		if (!isInitialized()) return null;
		return shipyard;
	}
	
	public static ConstructionInterface construction() {
		if (!isInitialized()) return null;
		return construction;
	}
	
	public static void printAPIStatus() {
		System.out.println("\u001b[38;2;150;150;150m---------API STATUS---------");
		System.out.println(isInitialized() ? "\u001b[32m" + "API initialized" : "\u001b[31m" + "API not Initialized!");
		System.out.println("\u001b[38;2;150;150;150m----------SERVICES----------");
		printServiceStatus("Discovery", discovery.isConnected());
		printServiceStatus("Authentication", authentication.isConnected());
		printServiceStatus("Meta", meta.isConnected());
		printServiceStatus("Matchmaking", matchmaking.isConnected());
		printServiceStatus("Game", game.isConnected());
		printServiceStatus("Shipyard", shipyard.isConnected());
		printServiceStatus("Construction", construction.isConnected());
	}
	
	private static void printServiceStatus(String serviceName, boolean connected) {
		System.out.println("\u001b[38;2;150;150;150m" + serviceName + ": " + getConnectedString(connected));
		System.out.println("\u001b[38;2;150;150;150m----------------------------\u001B[0m");
	}
	
	private static String getConnectedString(boolean connected) {
		return connected ? "\u001b[32m" + "connected" + "\u001b[0m" : "\u001b[31m" + "not connected" + "\u001b[0m";
	}

	private static boolean isInitialized() {
		if (discovery == null) {
			System.out.println("API not connected. Connect first via API.initialize()!");
			return false;
		}
		return true;
	}

}
