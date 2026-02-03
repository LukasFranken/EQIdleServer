package de.instinct.game.service.impl;

import java.util.List;

import de.instinct.engine.FleetEngine;
import de.instinct.engine.model.GameState;
import de.instinct.engine.order.GameOrder;
import de.instinct.engine.stats.StatCollector;
import de.instinct.engine.stats.model.GameStatistic;
import de.instinct.engine.util.EngineUtility;
import de.instinct.engine_api.core.model.GameStateInitialization;
import de.instinct.engine_api.core.service.GameStateInitializer;
import de.instinct.game.service.model.GameSession;

public class GameEngineInterface {
	
	private FleetEngine engine;
	private GameStateInitializer gameStateInitializer;
	
	public GameEngineInterface() {
		engine = new FleetEngine();
		engine.initialize();
		gameStateInitializer = new GameStateInitializer();
	}

	public void updateGameState(GameSession session) {
		long currentTime = System.currentTimeMillis();
		engine.update(session.getGameState(), currentTime - session.getLastUpdateTimeMS());
		session.setLastUpdateTimeMS(currentTime);
	}
	
	public GameState initializeGameState(GameStateInitialization gameStateInitialization) {
		System.out.println("Initializing game state with: " + gameStateInitialization);
		return gameStateInitializer.initialize(gameStateInitialization);
	}

	public void queue(GameState gameState, GameOrder gameOrder) {
		System.out.println("Queueing order: " + gameOrder);
		engine.queue(gameState, gameOrder);
	}

	public void queueAll(GameState gameState, List<GameOrder> gameOrders) {
		for (GameOrder gameOrder : gameOrders) {
			queue(gameState, gameOrder);
		}
	}
	
	public boolean containedValidOrders() {
		return engine.containedValidOrders();
	}

	public GameStatistic grabGameStatistic(String gameUUID) {
		return StatCollector.grab(gameUUID);
	}

	public boolean checkWiped(GameState gameState) {
		return EngineUtility.winIsWiped(gameState);
	}

}
