package de.instinct.matchmaker.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchmakingStatusResponse {
	
	public int foundPlayers;
	public int requiredPlayers;
	public String gameserverAddress;
	public int gameserverPort;

}
