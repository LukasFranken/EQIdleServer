package de.instinct.api.core;

import de.instinct.api.auth.service.impl.Authentication;
import de.instinct.api.commander.service.impl.Commander;
import de.instinct.api.construction.service.impl.Construction;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.api.core.logging.LoggingHook;
import de.instinct.api.discovery.service.impl.Discovery;
import de.instinct.api.game.service.impl.Game;
import de.instinct.api.matchmaking.service.impl.Matchmaking;
import de.instinct.api.meta.service.impl.Meta;
import de.instinct.api.mining.service.impl.Mining;
import de.instinct.api.shipyard.service.impl.Shipyard;
import de.instinct.api.shop.service.impl.Shop;
import de.instinct.api.social.service.impl.Social;
import de.instinct.api.starmap.service.impl.Starmap;

public class API {
	
	public static APIConfiguration configuration;
	public static String authKey;
	
	public static LoggingHook loggingHook = new LoggingHook() {
		
		@Override
		public void log(String message) {
			System.out.println(message);
		}
		
	};
	
	private static Discovery discovery;
	private static Authentication authentication;
	private static Meta meta;
	private static Matchmaking matchmaking;
	private static Game game;
	private static Shipyard shipyard;
	private static Construction construction;
	private static Shop shop;
	private static Starmap starmap;
	private static Commander commander;
	private static Social social;
	private static Mining mining;
	
	public static void initialize(APIConfiguration newConfiguration) {
		configuration = newConfiguration;
		discovery = new Discovery();
		authentication = new Authentication();
		meta = new Meta();
		matchmaking = new Matchmaking();
		game = new Game();
		shipyard = new Shipyard();
		construction = new Construction();
		shop = new Shop();
		starmap = new Starmap();
		commander = new Commander();
		social = new Social();
		mining = new Mining();
		
		if (newConfiguration != APIConfiguration.SERVER) {
			discovery().connect();
			authentication().connect();
			meta().connect();
			matchmaking().connect();
			game().connect();
			shipyard().connect();
			construction().connect();
			shop().connect();
			starmap().connect();
			commander().connect();
			social().connect();
			mining().connect();
			printAPIStatus();
		}
	}
	
	public static void setLoggingHook(LoggingHook newLoggingHook) {
		loggingHook = newLoggingHook;
	}
	
	public static Discovery discovery() {
		if (!apiReady()) return null;
		return discovery;
	}
	
	public static Authentication authentication() {
		if (!apiReady()) return null;
		return authentication;
	}
	
	public static Meta meta() {
		if (!apiReady()) return null;
		return meta;
	}
	
	public static Matchmaking matchmaking() {
		if (!apiReady()) return null;
		return matchmaking;
	}
	
	public static Game game() {
		if (!apiReady()) return null;
		return game;
	}
	
	public static Shipyard shipyard() {
		if (!apiReady()) return null;
		return shipyard;
	}
	
	public static Construction construction() {
		if (!apiReady()) return null;
		return construction;
	}
	
	public static Shop shop() {
		if (!apiReady()) return null;
		return shop;
	}
	
	public static Starmap starmap() {
		if (!apiReady()) return null;
		return starmap;
	}
	
	public static Commander commander() {
		if (!apiReady()) return null;
		return commander;
	}
	
	public static Social social() {
		if (!apiReady()) return null;
		return social;
	}
	
	public static Mining mining() {
		if (!apiReady()) return null;
		return mining;
	}
	
	public static boolean apiReady() {
		if (isInitialized()) return true;
		loggingHook.log("API not connected. Connect first via API.initialize()!");
		return false;
	}

	public static void printAPIStatus() {
		loggingHook.log("---------API STATUS---------");
		loggingHook.log(isInitialized() ? "API initialized" : "API not Initialized!");
		loggingHook.log("----------SERVICES----------");
		printServiceStatus("Discovery", discovery.isConnected());
		printServiceStatus("Authentication", authentication.isConnected());
		printServiceStatus("Meta", meta.isConnected());
		printServiceStatus("Matchmaking", matchmaking.isConnected());
		printServiceStatus("Game", game.isConnected());
		printServiceStatus("Shipyard", shipyard.isConnected());
		printServiceStatus("Construction", construction.isConnected());
		printServiceStatus("Shop", shop.isConnected());
		printServiceStatus("Starmap", starmap.isConnected());
		printServiceStatus("Commander", commander.isConnected());
		printServiceStatus("Social", social.isConnected());
		printServiceStatus("Mining", mining.isConnected());
	}
	
	private static void printServiceStatus(String serviceName, boolean connected) {
		loggingHook.log(serviceName + ": " + getConnectedString(connected));
		loggingHook.log("----------------------------");
	}
	
	private static String getConnectedString(boolean connected) {
		return connected ? "connected" : "not connected";
	}
	
	public static boolean isInitialized() {
		if (discovery == null) {
			return false;
		}
		return true;
	}

}
