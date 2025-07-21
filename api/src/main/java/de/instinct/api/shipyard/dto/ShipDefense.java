 package de.instinct.api.shipyard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipDefense {
	
	private float shieldRegenerationSpeed;
	private float shield;
	private float armor;

}
