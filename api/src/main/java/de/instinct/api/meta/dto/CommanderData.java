package de.instinct.api.meta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommanderData {
	
	private float commandPointsGenerationSpeed;
	private float maxCommandPoints;
	private float startCommandPoints;

}
