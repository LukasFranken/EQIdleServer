package de.instinct.api.matchmaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchmakingStatusResponse {
	
	private MatchmakingStatusResponseCode code;
	private int foundPlayers;
	private int requiredPlayers;
	private String gameserverAddress;
	private int gameserverPort;

}
