package de.instinct.api.shipyard.dto.ship.component.level;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ComponentLevel;
import de.instinct.api.shipyard.dto.ship.component.attribute.ShieldAttribute;
import de.instinct.api.shipyard.dto.ship.component.types.shield.ShieldRequirementType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShieldLevel extends ComponentLevel {

	private ShieldRequirementType requirementType;
	private List<ShieldAttribute> attributes;

}
