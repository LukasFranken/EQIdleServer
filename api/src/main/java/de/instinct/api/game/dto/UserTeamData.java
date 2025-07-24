package de.instinct.api.game.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class UserTeamData {
	
	private String uuid;
	private int teamId;

}
