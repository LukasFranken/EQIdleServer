package de.instinct.game.service.impl;

import java.util.List;

import de.instinct.engine.FleetEngine;
import de.instinct.engine.initialization.GameStateInitialization;
import de.instinct.engine.model.GameState;
import de.instinct.engine.order.GameOrder;
import de.instinct.game.service.model.GameSession;

public class GameEngineInterface {
	
	private FleetEngine engine;
	
	public GameEngineInterface() {
		engine = new FleetEngine();
		engine.initialize();
	}

	public void updateGameState(GameSession session) {
		long currentTime = System.currentTimeMillis();
		engine.update(session.getGameState(), currentTime - session.getLastUpdateTimeMS());
		session.setLastUpdateTimeMS(currentTime);
	}
	
	public GameState initializeGameState(GameStateInitialization gameStateInitialization) {
		return engine.initializeGameState(gameStateInitialization);
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

}
