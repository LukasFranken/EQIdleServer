package de.instinct.api.shipyard.dto.ship.component.level;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ComponentLevel;
import de.instinct.api.shipyard.dto.ship.component.types.weapon.WeaponRequirementType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class WeaponLevel extends ComponentLevel {
	
	private WeaponRequirementType requirementType;

}
