package de.instinct.matchmaker.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchmakingStatusResponse {
	
	private MatchmakingStatusResponseCode code;
	private int foundPlayers;
	private int requiredPlayers;
	private String gameserverAddress;
	private int gameserverPort;

}
