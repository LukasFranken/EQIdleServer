package de.instinct.api.mining.dto.player;

import de.instinct.api.core.annotation.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Dto
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiningPlayerFeatureData {
	
	private boolean aimDirectionRay;
	private boolean rangeIndicator;
	private boolean minimap;
	private boolean map;
	private boolean healthBars;
	private boolean asteroidInfoPanel;

}
