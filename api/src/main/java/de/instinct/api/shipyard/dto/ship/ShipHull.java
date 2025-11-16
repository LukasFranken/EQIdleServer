package de.instinct.api.shipyard.dto.ship;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.level.HullLevel;
import de.instinct.engine.model.ship.components.types.HullType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShipHull extends ShipComponent {
	
	private HullType type;
	private List<HullLevel> levels;

}
