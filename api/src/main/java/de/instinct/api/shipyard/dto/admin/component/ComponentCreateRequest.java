package de.instinct.api.shipyard.dto.admin.component;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ShipComponentType;
import lombok.Data;

@Dto
@Data
public class ComponentCreateRequest {
	
	private String shipname;
	private ShipComponentType type;
	private String componentType;

}
