package de.instinct.api.shipyard.dto.ship.component.attribute;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ComponentAttribute;
import de.instinct.api.shipyard.dto.ship.component.types.weapon.WeaponAttributeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class WeaponAttribute extends ComponentAttribute {

	private WeaponAttributeType type;
	
}
