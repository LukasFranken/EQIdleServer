package de.instinct.api.game.dto;

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
public class GameSessionInitializationRequest {
	
	private List<UserData> users;
	private GameType type;

}
