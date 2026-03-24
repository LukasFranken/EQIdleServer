package de.instinct.api.construction.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class Infrastructure {
	
	private float basePlanetResourceGenerationSpeed;
	private int baseTurretSlots;
	private List<PlanetTurretBlueprint> turretBlueprints;

}
