package de.instinct.api.commander.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class BaseCommanderData {
	
	private List<RankUpCommanderUpgrade> rankUpData;
	private int baseMaxResources;
	private int baseStartResources;
	private float baseResourceGenerationSpeed;

}
