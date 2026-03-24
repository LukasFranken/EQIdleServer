package de.instinct.api.construction.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlanetTurretBlueprint {
	
	private int id;
	private String name;
	private int cost;
	private PlanetDefense planetDefense;
	private PlanetWeapon planetWeapon;

}
