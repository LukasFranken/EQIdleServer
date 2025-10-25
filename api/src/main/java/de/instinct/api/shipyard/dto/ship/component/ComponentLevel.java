package de.instinct.api.shipyard.dto.ship.component;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public abstract class ComponentLevel {
	
	private int level;
	private float requirementValue;

}
