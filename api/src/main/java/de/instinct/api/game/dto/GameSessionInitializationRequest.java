package de.instinct.api.game.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.matchmaking.model.GameType;
import lombok.Data;

@Dto
@Data
public class GameSessionInitializationRequest {
	
	private List<UserTeamData> users;
	private GameType type;

}
