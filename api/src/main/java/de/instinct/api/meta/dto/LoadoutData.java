package de.instinct.api.meta.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadoutData {
	
	private double resourceGenerationSpeed;
	private double commandPointsGenerationSpeed;
	private double maxCommandPoints;
	private double maxPlanetCapacity;
	private double startCommandPoints;
	
	private List<ShipData> ships;

}
