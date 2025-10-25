package de.instinct.api.shipyard.dto.ship.component.attribute;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ComponentAttribute;
import de.instinct.api.shipyard.dto.ship.component.types.engine.EngineAttributeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class EngineAttribute extends ComponentAttribute {

	private EngineAttributeType type;
	
}
