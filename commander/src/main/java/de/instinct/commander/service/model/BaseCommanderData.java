package de.instinct.commander.service.model;

import java.util.List;

import de.instinct.api.commander.dto.RankUpCommanderUpgrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseCommanderData {
	
	private List<RankUpCommanderUpgrade> rankUpData;
	private int baseMaxCommandPoints;
	private int baseStartCommandPoints;
	private float baseCommandPointsPerSecond;

}
