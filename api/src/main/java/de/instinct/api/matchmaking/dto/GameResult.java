package de.instinct.api.matchmaking.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {
	
	private long playedMS;
	private List<PlayerReward> rewards;

}
