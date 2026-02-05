package de.instinct.api.matchmaking.dto;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.ShipComponentType;
import lombok.Data;

@Dto
@Data
public class ShipComponentResult {
	
	private ShipComponentType type;
	private float startProgress;
	private float endProgress;
	private float maxProgress;

}
