package de.instinct.api.matchmaking.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class MatchmakingStatusResponse {
	
	private MatchmakingStatusResponseCode code;
	private int foundPlayers;
	private int requiredPlayers;
	private String gameserverAddress;
	private int gameserverPort;

}
