package de.instinct.engine_api.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.instinct.engine.core.meta.data.MetaData;
import de.instinct.engine.core.meta.data.PauseData;
import de.instinct.engine.core.player.Player;
import de.instinct.engine.core.player.data.PlayerData;
import de.instinct.engine.fleet.FleetEngine;
import de.instinct.engine.fleet.data.FleetGameState;
import de.instinct.engine.fleet.data.ResultData;
import de.instinct.engine.fleet.data.StaticData;
import de.instinct.engine.fleet.entity.data.EntityData;
import de.instinct.engine.fleet.entity.planet.Planet;
import de.instinct.engine.fleet.net.data.PlayerConnectionStatus;
import de.instinct.engine.fleet.order.data.OrderData;
import de.instinct.engine.fleet.stats.StatCollector;
import de.instinct.engine_api.core.model.GameStateInitialization;
import de.instinct.engine_api.core.model.PlanetInitialization;

public class GameStateInitializer {
	
	private FleetEngine fleetEngine;
	
	public GameStateInitializer() {
		fleetEngine = new FleetEngine();
	}
	
	public FleetGameState initialize(GameStateInitialization initialization) {
		StatCollector.initialize(initialization.getGameUUID(), initialization.getPlayers());
		FleetGameState state = new FleetGameState();
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
		
		state.metaData = new MetaData();
		state.metaData.pauseData = new PauseData();
		state.metaData.pauseData.resumeCountdownMS = 3000L;
		state.metaData.pauseData.teamPausesMS = new HashMap<>();
		state.metaData.pauseData.teamPausesMS.put(0, 0L);
		state.metaData.pauseData.teamPausesMS.put(1, 0L);
		state.metaData.pauseData.teamPausesMS.put(2, 0L);
		state.metaData.pauseData.teamPausesCount = new HashMap<>();
		state.metaData.pauseData.teamPausesCount.put(0, 0);
		state.metaData.pauseData.teamPausesCount.put(1, initialization.getPauseCountLimit());
		state.metaData.pauseData.teamPausesCount.put(2, initialization.getPauseCountLimit());
		state.metaData.pauseData.maxPauseMS = initialization.getPauseTimeLimitMS();
		
		state.staticData = new StaticData();
		state.staticData.ancientPlanetResourceDegradationFactor = initialization.getMap().getAncientPlanetResourceDegradationFactor();
		state.staticData.zoomFactor = initialization.getMap().getZoomFactor();
		state.staticData.maxGameTimeMS = initialization.getGameTimeLimitMS();
		state.staticData.atpToWin = initialization.getAtpToWin();
		state.playerData = new PlayerData();
		state.playerData.players = initialization.getPlayers();
		state.playerData.connectionStati = generateConnectionStati(initialization.getPlayers());
		
		fleetEngine.initialize(state);
		return state;
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

	private List<Planet> generateInitialPlanets(GameStateInitialization initialization, FleetGameState state) {
		List<Planet> initialPlanets = new ArrayList<>();
		for (PlanetInitialization init : initialization.getMap().getPlanets()) {
			Player planetOwner = EngineDataInterface.getPlayer(initialization.getPlayers(), init.getOwnerId());
			Planet initialPlanet = EngineDataInterface.createPlanet(state, planetOwner, init.getPosition(), init.isAncient());
			initialPlanets.add(initialPlanet);
		}
		return initialPlanets;
	}

}
