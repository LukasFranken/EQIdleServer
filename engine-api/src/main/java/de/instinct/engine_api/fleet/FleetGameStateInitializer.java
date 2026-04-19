package de.instinct.engine_api.fleet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.instinct.engine.core.player.Player;
import de.instinct.engine.fleet.FleetEngine;
import de.instinct.engine.fleet.data.FleetGameState;
import de.instinct.engine.fleet.data.ResultData;
import de.instinct.engine.fleet.data.StaticData;
import de.instinct.engine.fleet.entity.data.FleetEntityData;
import de.instinct.engine.fleet.entity.planet.Planet;
import de.instinct.engine.fleet.stats.StatCollector;
import de.instinct.engine_api.core.service.EngineDataInterface;
import de.instinct.engine_api.core.service.GameStateInitializer;
import de.instinct.engine_api.fleet.model.FleetGameMap;
import de.instinct.engine_api.fleet.model.FleetGameStateInitialization;
import de.instinct.engine_api.fleet.model.PlanetInitialization;

public class FleetGameStateInitializer extends GameStateInitializer {
	
	private FleetEngine fleetEngine;
	
	public FleetGameStateInitializer() {
		fleetEngine = new FleetEngine();
	}
	
	public FleetGameState initializeFleet(FleetGameStateInitialization initialization) {
		StatCollector.initialize(initialization.getGameUUID(), initialization.getPlayers());
		FleetGameState state = new FleetGameState();
		super.initialize(state, initialization);
		state.teamATPs = new HashMap<>();
		state.teamATPs.put(0, 0f);
		state.teamATPs.put(1, 0f);
		state.teamATPs.put(2, 0f);
		
		state.resultData = new ResultData();
		state.resultData.winner = 0;
		state.resultData.surrendered = 0;
		state.resultData.wiped = false;
		
		state.staticData = new StaticData();
		state.staticData.maxGameTimeMS = initialization.getGameTimeLimitMS();
		state.staticData.atpToWin = initialization.getAtpToWin();
		
		state.entityData = new FleetEntityData();
		state.entityData.planets = generateInitialPlanets(state, initialization);
		state.entityData.ships = new ArrayList<>();
		state.entityData.turrets = new ArrayList<>();
		state.entityData.projectiles = new ArrayList<>();
		
		fleetEngine.initialize(state);
		return state;
	}
	
	private List<Planet> generateInitialPlanets(FleetGameState state, FleetGameStateInitialization initialization) {
		List<Planet> initialPlanets = new ArrayList<>();
		FleetGameMap map = (FleetGameMap) initialization.getMap();
		state.staticData.ancientPlanetResourceDegradationFactor = map.getAncientPlanetResourceDegradationFactor();
		state.staticData.zoomFactor = map.getZoomFactor();
		for (PlanetInitialization init : map.getPlanets()) {
			Player planetOwner = EngineDataInterface.getPlayer(initialization.getPlayers(), init.getOwnerId());
			Planet initialPlanet = EngineDataInterface.createPlanet(state, planetOwner, init.getPosition(), init.isAncient());
			initialPlanets.add(initialPlanet);
		}
		return initialPlanets;
	}

}
