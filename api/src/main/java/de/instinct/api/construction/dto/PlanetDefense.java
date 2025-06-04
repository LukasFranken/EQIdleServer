package de.instinct.api.construction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanetDefense {
	
	private float shieldRegenerationSpeed;
	private float shield;
	private float armor;

}
