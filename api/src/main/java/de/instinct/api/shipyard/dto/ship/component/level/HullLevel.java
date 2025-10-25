package de.instinct.api.shipyard.dto.ship.component.level;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ComponentLevel;
import de.instinct.api.shipyard.dto.ship.component.attribute.HullAttribute;
import de.instinct.api.shipyard.dto.ship.component.types.hull.HullRequirementType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class HullLevel extends ComponentLevel {

	private HullRequirementType requirementType;
	private List<HullAttribute> attributes;

}
