package de.instinct.api.shipyard.dto.ship.component.level;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ComponentLevel;
import de.instinct.api.shipyard.dto.ship.component.attribute.EngineAttribute;
import de.instinct.api.shipyard.dto.ship.component.types.engine.EngineRequirementType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class EngineLevel extends ComponentLevel {

	private EngineRequirementType requirementType;
	private List<EngineAttribute> attributes;

}
