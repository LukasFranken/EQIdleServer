package de.instinct.game.service.model;

import java.util.List;

import de.instinct.api.matchmaking.model.GameType;
import de.instinct.engine.model.GameState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameSession {
	
	private String uuid;
	private GameType gameType;
	private List<User> users;
	private GameState gameState;
	private long lastUpdateTimeMS;
	private boolean active;

}
