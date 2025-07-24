 package de.instinct.api.shipyard.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShipDefense {
	
	private float shieldRegenerationSpeed;
	private float shield;
	private float armor;

}
