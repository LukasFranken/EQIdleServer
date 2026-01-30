package de.instinct.api.shipyard.dto.ship;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.types.ShipHullType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShipHull extends ShipComponent {
	
	private ShipHullType type;

}
