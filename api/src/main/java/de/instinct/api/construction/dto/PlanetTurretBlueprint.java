package de.instinct.api.construction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanetTurretBlueprint {
	
	private String uuid;
	private String name;
	private PlanetDefense planetDefense;
	private PlanetWeapon planetWeapon;
	
	private boolean inUse;

}
