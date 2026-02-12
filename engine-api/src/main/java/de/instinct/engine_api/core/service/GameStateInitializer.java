package de.instinct.engine_api.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.instinct.engine.combat.CombatProcessor;
import de.instinct.engine.model.GameState;
import de.instinct.engine.model.Player;
import de.instinct.engine.model.PlayerConnectionStatus;
import de.instinct.engine.model.data.EntityData;
import de.instinct.engine.model.data.OrderData;
import de.instinct.engine.model.data.PauseData;
import de.instinct.engine.model.data.PlayerData;
import de.instinct.engine.model.data.ResultData;
import de.instinct.engine.model.data.StaticData;
import de.instinct.engine.model.planet.Planet;
import de.instinct.engine.planet.PlanetProcessor;
import de.instinct.engine.stats.StatCollector;
import de.instinct.engine.util.EngineUtility;
import de.instinct.engine_api.core.model.GameStateInitialization;
import de.instinct.engine_api.core.model.PlanetInitialization;

public class GameStateInitializer {
	
	private CombatProcessor combatProcessor;
	private PlanetProcessor planetProcessor;
	
	public GameStateInitializer() {
		combatProcessor = new CombatProcessor();
		planetProcessor = new PlanetProcessor();
	}
	
	public GameState initialize(GameStateInitialization initialization) {
		StatCollector.initialize(initialization.getGameUUID(), initialization.getPlayers());
		GameState state = new GameState();
		state.gameUUID = initialization.getGameUUID();
		state.gameTimeMS = 0;
		state.started = false;
		state.teamATPs = new HashMap<>();
		state.teamATPs.put(0, 0D);
		state.teamATPs.put(1, 0D);
		state.teamATPs.put(2, 0D);
		
		state.resultData = new ResultData();
		state.resultData.winner = 0;
		state.resultData.surrendered = 0;
		state.resultData.wiped = false;
		
		state.entityData = new EntityData();
		state.entityData.entityCounter = 0;
		state.entityData.planets = generateInitialPlanets(initialization, state);
		state.entityData.ships = new ArrayList<>();
		state.entityData.turrets = new ArrayList<>();
		state.entityData.projectiles = new ArrayList<>();
		
		state.orderData = new OrderData();
		state.orderData.unprocessedOrders = new ConcurrentLinkedQueue<>();
		state.orderData.processedOrders = new ArrayList<>();
		
		state.pauseData = new PauseData();
		state.pauseData.resumeCountdownMS = 3000L;
		state.pauseData.teamPausesMS = new HashMap<>();
		state.pauseData.teamPausesMS.put(0, 0L);
		state.pauseData.teamPausesMS.put(1, 0L);
		state.pauseData.teamPausesMS.put(2, 0L);
		state.pauseData.teamPausesCount = new HashMap<>();
		state.pauseData.teamPausesCount.put(0, 0);
		state.pauseData.teamPausesCount.put(1, initialization.getPauseCountLimit());
		state.pauseData.teamPausesCount.put(2, initialization.getPauseCountLimit());
		
		state.staticData = new StaticData();
		state.staticData.ancientPlanetResourceDegradationFactor = initialization.getAncientPlanetResourceDegradationFactor();
		state.staticData.zoomFactor = initialization.getMap().getZoomFactor();
		state.staticData.maxGameTimeMS = initialization.getGameTimeLimitMS();
		state.staticData.atpToWin = initialization.getAtpToWin();
		state.staticData.playerData = new PlayerData();
		state.staticData.playerData.players = initializePlayers(state.gameUUID, initialization.getPlayers());
		state.staticData.playerData.connectionStati = generateConnectionStati(initialization.getPlayers());
		state.staticData.maxPauseMS = initialization.getPauseTimeLimitMS();
		state.staticData.minPauseMS = 1000L;
		combatProcessor.initialize(state);
		return state;
	}
	
	private List<Player> initializePlayers(String gameUUID, List<Player> players) {
		for (Player player : players) {
			player.currentCommandPoints = player.startCommandPoints;
		}
		return players;
	}

	private List<PlayerConnectionStatus> generateConnectionStati(List<Player> players) {
		List<PlayerConnectionStatus> connectionStati = new ArrayList<>();
		for (Player player : players) {
			PlayerConnectionStatus status = new PlayerConnectionStatus();
			status.playerId = player.id;
			connectionStati.add(status);
		}
		return connectionStati;
	}

	private List<Planet> generateInitialPlanets(GameStateInitialization initialization, GameState state) {
		List<Planet> initialPlanets = new ArrayList<>();
		for (PlanetInitialization init : initialization.getMap().getPlanets()) {
			Player planetOwner = EngineUtility.getPlayer(initialization.getPlayers(), init.getOwnerId());
			Planet initialPlanet = planetProcessor.createPlanet(planetOwner.planetData, state);
			initialPlanet.ownerId = init.getOwnerId();
			initialPlanet.position = init.getPosition();
			if (init.isAncient()) {
				initialPlanet.ancient = true;
			}
			initialPlanets.add(initialPlanet);
		}
		return initialPlanets;
	}

}
