package de.instinct.matchmaker.controller.dto;

import de.instinct.matchmaker.service.model.enums.FactionMode;
import de.instinct.matchmaker.service.model.enums.GameMode;
import de.instinct.matchmaker.service.model.enums.VersusMode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchmakingRegistrationRequest {
	
	public VersusMode versusMode;
	public GameMode gameMode;
	public FactionMode factionMode;

}
