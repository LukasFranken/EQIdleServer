package de.instinct.matchmaker.controller.dto;

import base.game.model.enums.FactionMode;
import base.game.model.enums.GameMode;
import base.game.model.enums.VersusMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchmakingRegistrationRequest {
	
	private VersusMode versusMode;
	private GameMode gameMode;
	private FactionMode factionMode;

}