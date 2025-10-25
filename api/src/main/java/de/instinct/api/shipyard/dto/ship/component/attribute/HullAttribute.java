package de.instinct.api.shipyard.dto.ship.component.attribute;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ComponentAttribute;
import de.instinct.api.shipyard.dto.ship.component.types.hull.HullAttributeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class HullAttribute extends ComponentAttribute {

	private HullAttributeType type;
	
}
