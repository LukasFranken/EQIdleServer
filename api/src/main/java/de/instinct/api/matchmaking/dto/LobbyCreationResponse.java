package de.instinct.api.matchmaking.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class LobbyCreationResponse {

	private String lobbyUUID;
	
}
