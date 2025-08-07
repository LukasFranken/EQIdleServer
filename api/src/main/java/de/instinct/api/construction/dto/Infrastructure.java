package de.instinct.api.construction.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class Infrastructure {
	
	private float baseResourceGenerationSpeed;
	private float baseMaxResourceCapacity;
	private List<PlanetTurretBlueprint> turretBlueprints;

}
