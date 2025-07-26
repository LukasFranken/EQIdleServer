package de.instinct.api.commander.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.meta.dto.PlayerRank;
import lombok.Data;

@Dto
@Data
public class RankUpCommanderUpgrade {
	
	private PlayerRank playerRank;
	private List<CommanderUpgrade> upgrades;

}
