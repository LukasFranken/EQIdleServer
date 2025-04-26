package de.instinct.api.game.dto;

import java.util.List;

import de.instinct.api.matchmaking.model.GameType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameSessionInitializationRequest {
	
	private String lobbyUUID;
	private List<String> playerUUIDs;
	private GameType type;

}
