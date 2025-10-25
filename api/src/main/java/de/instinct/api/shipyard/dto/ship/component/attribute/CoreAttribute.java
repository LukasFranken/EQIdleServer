package de.instinct.api.shipyard.dto.ship.component.attribute;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ComponentAttribute;
import de.instinct.api.shipyard.dto.ship.component.types.core.CoreAttributeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class CoreAttribute extends ComponentAttribute {
	
	private CoreAttributeType type;

}
