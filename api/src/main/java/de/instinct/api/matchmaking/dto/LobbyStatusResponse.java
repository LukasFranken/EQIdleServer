package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.matchmaking.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LobbyStatusResponse {
	
	private LobbyStatusCode code;
	private GameType type;
	private List<String> userNames;

}
