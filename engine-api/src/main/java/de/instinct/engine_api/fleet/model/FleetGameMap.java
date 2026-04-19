package de.instinct.engine_api.fleet.model;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.engine_api.core.model.GameMap;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class FleetGameMap extends GameMap {

	private float ancientPlanetResourceDegradationFactor;
	private List<PlanetInitialization> planets;
	private float zoomFactor;

}
