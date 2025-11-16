package de.instinct.api.shipyard.dto.ship;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.level.EngineLevel;
import de.instinct.engine.model.ship.components.types.EngineType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShipEngine extends ShipComponent {
	
	private EngineType type;
	private List<EngineLevel> levels;

}
