package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.matchmaking.model.GameType;
import lombok.Data;

@Dto
@Data
public class LobbyStatusResponse {
	
	private LobbyStatusCode code;
	private GameType type;
	private List<String> userNames;

}
