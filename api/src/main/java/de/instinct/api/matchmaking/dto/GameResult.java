package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class GameResult {
	
	private long playedMS;
	private List<PlayerReward> rewards;

}
