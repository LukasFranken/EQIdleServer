package de.instinct.api.shipyard.dto.ship;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlayerShipComponentLevel {
	
	private int componentId;
	private int level;
	private float progress;

}
