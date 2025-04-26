package de.instinct.game.service.model;

import java.util.List;

import de.instinct.api.matchmaking.model.GameType;
import de.instinct.engine.model.GameState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameSession {
	
	private GameType gameType;
	private List<PlayerConnection> playerConnections;
	private GameState gameState;
	private long lastUpdateTimeMS;
	private boolean active;
	private boolean matchmakingComplete;

}
