package de.instinct.api.shipyard.dto.ship;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.types.ShipEngineType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShipEngine extends ShipComponent {
	
	private ShipEngineType type;

}
