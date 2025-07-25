package de.instinct.api.commander.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class CommanderData {
	
	private float commandPointsGenerationSpeed;
	private float maxCommandPoints;
	private float startCommandPoints;

}
