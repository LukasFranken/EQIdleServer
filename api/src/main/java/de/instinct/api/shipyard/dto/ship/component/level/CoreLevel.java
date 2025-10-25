package de.instinct.api.shipyard.dto.ship.component.level;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ComponentLevel;
import de.instinct.api.shipyard.dto.ship.component.attribute.CoreAttribute;
import de.instinct.api.shipyard.dto.ship.component.types.core.CoreRequirementType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class CoreLevel extends ComponentLevel {

	private CoreRequirementType reqirementType;
	private List<CoreAttribute> attributes;

}
