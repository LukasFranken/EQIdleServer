package de.instinct.engine_api.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.instinct.engine.combat.CombatProcessor;
import de.instinct.engine.model.GameState;
import de.instinct.engine.model.Player;
import de.instinct.engine.model.PlayerConnectionStatus;
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
		state.orders = new ArrayList<>();
		state.unprocessedOrders = new ConcurrentLinkedQueue<>();
		state.entityCounter = 0;
		state.orderCounter = 0;
		state.gameUUID = initialization.getGameUUID();
		state.players = initializePlayers(state.gameUUID, initialization.getPlayers());
		state.connectionStati = generateConnectionStati(initialization.getPlayers());
		state.planets = generateInitialPlanets(initialization, state);
		state.zoomFactor = initialization.getMap().getZoomFactor();
		state.ships = new ArrayList<>();
		state.turrets = new ArrayList<>();
		state.projectiles = new ArrayList<>();
		state.gameTimeMS = 0;
		state.maxGameTimeMS = initialization.getGameTimeLimitMS();
		state.winner = 0;
		state.atpToWin = initialization.getAtpToWin();
		state.teamATPs = new HashMap<>();
		state.teamATPs.put(0, 0D);
		state.teamATPs.put(1, 0D);
		state.teamATPs.put(2, 0D);
		state.ancientPlanetResourceDegradationFactor = initialization.getAncientPlanetResourceDegradationFactor();
		state.started = false;
		state.maxPauseMS = initialization.getPauseTimeLimitMS();
		state.minPauseMS = 1000L;
		state.resumeCountdownMS = 3000L;
		state.teamPausesMS = new HashMap<>();
		state.teamPausesMS.put(0, 0L);
		state.teamPausesMS.put(1, 0L);
		state.teamPausesMS.put(2, 0L);
		state.teamPausesCount = new HashMap<>();
		state.teamPausesCount.put(0, 0);
		state.teamPausesCount.put(1, initialization.getPauseCountLimit());
		state.teamPausesCount.put(2, initialization.getPauseCountLimit());
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
