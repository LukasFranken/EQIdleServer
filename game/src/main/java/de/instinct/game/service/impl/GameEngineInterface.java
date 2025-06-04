package de.instinct.game.service.impl;

import java.util.List;

import de.instinct.engine.FleetEngine;
import de.instinct.engine.initialization.GameStateInitialization;
import de.instinct.engine.model.GameState;
import de.instinct.engine.order.GameOrder;
import de.instinct.engine.util.EngineUtility;
import de.instinct.game.service.model.GameSession;

public class GameEngineInterface {
	
	private FleetEngine engine;
	
	public GameEngineInterface() {
		engine = new FleetEngine();
		engine.initialize();
	}

	public boolean updateGameState(GameSession session) {
		boolean containedValidOrder = false;
		long currentTime = System.currentTimeMillis();
		containedValidOrder = engine.update(session.getGameState(), currentTime - session.getLastUpdateTimeMS());
		session.setLastUpdateTimeMS(currentTime);
		EngineUtility.checkVictory(session.getGameState());
		return containedValidOrder || session.getGameState().winner != 0;
	}
	
	public GameState initializeGameState(GameStateInitialization gameStateInitialization) {
		return engine.initializeGameState(gameStateInitialization);
	}

	public void queue(GameState gameState, GameOrder gameOrder) {
		engine.queue(gameState, gameOrder);
	}

	public void queueAll(GameState gameState, List<GameOrder> gameOrders) {
		for (GameOrder gameOrder : gameOrders) {
			queue(gameState, gameOrder);
		}
	}

}
