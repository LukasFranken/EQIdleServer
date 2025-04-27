package de.instinct.api.meta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loadout {
	
	private double fleetMovementSpeed;
	private double resourceGenerationSpeed;
	private double commandPointsGenerationSpeed;
	private double maxCommandPoints;
	private double maxPlanetCapacity;
	private double startCommandPoints;

}
