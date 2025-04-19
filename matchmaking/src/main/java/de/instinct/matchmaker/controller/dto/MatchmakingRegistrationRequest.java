package de.instinct.matchmaker.controller.dto;

import de.instinct.matchmaker.service.model.enums.FactionMode;
import de.instinct.matchmaker.service.model.enums.GameMode;
import de.instinct.matchmaker.service.model.enums.VersusMode;
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