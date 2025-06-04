package de.instinct.api.construction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Infrastructure {
	
	private float resourceGenerationSpeed;
	private float maxResourceCapacity;
	private float percentOfArmorAfterCapture;
	private PlanetDefense planetDefense;
	private PlanetWeapon planetWeapon;

}
