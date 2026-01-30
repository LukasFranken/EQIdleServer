package de.instinct.api.shipyard.dto.ship;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.types.ShipCoreType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShipCore extends ShipComponent {
	
	private ShipCoreType type;

}