package de.instinct.game.service.model;

import java.util.List;

import de.instinct.api.matchmaking.model.GameType;
import de.instinct.engine.fleet.data.FleetGameState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameSession {
	
	private String uuid;
	private GameType gameType;
	private List<User> users;
	private FleetGameState gameState;
	private long lastUpdateTimeMS;
	private long lastClientUpdateTimeMS;
	private boolean active;

}
