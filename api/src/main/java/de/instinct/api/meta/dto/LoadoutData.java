package de.instinct.api.meta.dto;

import java.util.List;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.shipyard.dto.ShipBlueprint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadoutData {
	
	private double commandPointsGenerationSpeed;
	private double maxCommandPoints;
	private double startCommandPoints;
	
	private Infrastructure infrastructure;
	private List<ShipBlueprint> ships;

}
