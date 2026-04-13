package de.instinct.game.service.impl;

import java.util.List;

import de.instinct.engine.core.order.GameOrder;
import de.instinct.engine.fleet.FleetEngine;
import de.instinct.engine.fleet.data.FleetGameState;
import de.instinct.engine.fleet.stats.StatCollector;
import de.instinct.engine.fleet.stats.model.GameStatistic;
import de.instinct.engine_api.core.model.GameStateInitialization;
import de.instinct.engine_api.core.service.GameStateInitializer;
import de.instinct.game.service.model.GameSession;

public class GameEngineInterface {
	
	private FleetEngine fleetEngine;
	private GameStateInitializer gameStateInitializer;
	
	public GameEngineInterface() {
		fleetEngine = new FleetEngine();
		gameStateInitializer = new GameStateInitializer();
	}

	public void updateGameState(GameSession session) {
		long currentTime = System.currentTimeMillis();
		fleetEngine.update(session.getGameState(), currentTime - session.getLastUpdateTimeMS());
		session.setLastUpdateTimeMS(currentTime);
	}
	
	public FleetGameState initializeGameState(GameStateInitialization gameStateInitialization) {
		System.out.println("Initializing game state with: " + gameStateInitialization);
		return gameStateInitializer.initialize(gameStateInitialization);
	}

	public void queue(FleetGameState gameState, GameOrder gameOrder) {
		System.out.println("Queueing order: " + gameOrder);
		fleetEngine.queue(gameState, gameOrder);
	}

	public void queueAll(FleetGameState gameState, List<GameOrder> gameOrders) {
		for (GameOrder gameOrder : gameOrders) {
			queue(gameState, gameOrder);
		}
	}

	public GameStatistic grabGameStatistic(String gameUUID) {
		return StatCollector.grab(gameUUID);
	}

	public boolean checkSurrendered(FleetGameState gameState) {
		return gameState.resultData.surrendered != 0;
	}

}
