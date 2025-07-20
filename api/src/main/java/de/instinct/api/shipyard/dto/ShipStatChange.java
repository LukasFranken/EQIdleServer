package de.instinct.api.shipyard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipStatChange {
	
	private ShipStat stat;
	private float value;

}
