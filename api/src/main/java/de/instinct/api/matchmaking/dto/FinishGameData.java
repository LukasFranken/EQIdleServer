package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class FinishGameData {
	
	private long playedMS;
	private int winnerTeamId;
	private boolean wiped;
	private boolean surrendered;
	private List<PlayerShipResult> playerShipResults;

}
