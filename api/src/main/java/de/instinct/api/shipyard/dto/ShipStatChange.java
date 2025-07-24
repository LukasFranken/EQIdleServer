package de.instinct.api.shipyard.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShipStatChange {
	
	private ShipStat stat;
	private float value;

}
