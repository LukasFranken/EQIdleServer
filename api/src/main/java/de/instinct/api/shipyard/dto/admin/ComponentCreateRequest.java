package de.instinct.api.shipyard.dto.admin;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ShipComponentType;
import lombok.Data;

@Dto
@Data
public class ComponentCreateRequest {
	
	private String name;
	private ShipComponentType type;
	private String componentType;

}
