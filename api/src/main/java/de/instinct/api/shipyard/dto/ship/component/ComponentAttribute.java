package de.instinct.api.shipyard.dto.ship.component;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public abstract class ComponentAttribute {
	
	private double value;

}
