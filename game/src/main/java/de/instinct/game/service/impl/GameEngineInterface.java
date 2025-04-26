package de.instinct.game.service.impl;

import java.util.List;

import de.instinct.engine.EngineUtility;
import de.instinct.engine.EventEngine;
import de.instinct.engine.model.GameState;
import de.instinct.engine.order.GameOrder;
import de.instinct.game.service.model.GameSession;

public class GameEngineInterface {
	
	private EventEngine engine;
	
	public GameEngineInterface() {
		engine = new EventEngine();
	}

	public boolean updateGameState(GameSession session) {
		boolean containedValidOrder = false;
		long currentTime = System.currentTimeMillis();
		containedValidOrder = engine.update(session.getGameState(), currentTime - session.getLastUpdateTimeMS());
		session.setLastUpdateTimeMS(currentTime);
		EngineUtility.checkVictory(session.getGameState());
		return containedValidOrder || session.getGameState().winner != 0;
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
