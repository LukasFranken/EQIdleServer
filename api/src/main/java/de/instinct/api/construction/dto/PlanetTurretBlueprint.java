package de.instinct.api.construction.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlanetTurretBlueprint {
	
	private String uuid;
	private String name;
	private PlanetDefense planetDefense;
	private PlanetWeapon planetWeapon;
	
	private boolean inUse;

}
