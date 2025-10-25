package de.instinct.api.shipyard.dto.ship;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.level.CoreLevel;
import de.instinct.api.shipyard.dto.ship.component.types.core.CoreType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShipCore extends ShipComponent {
	
	private CoreType type;
	private List<CoreLevel> levels;

}
