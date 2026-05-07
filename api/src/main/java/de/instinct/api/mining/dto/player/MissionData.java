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
public class MissionData {
	
	private String name;
	private int minedAsteroids;
	private boolean completed;

}
