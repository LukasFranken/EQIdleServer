package de.instinct.api.matchmaking.dto;

import de.instinct.api.matchmaking.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchmakingRegistrationRequest {
	
	private GameType gameType;

}