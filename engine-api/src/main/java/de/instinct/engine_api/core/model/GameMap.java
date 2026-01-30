package de.instinct.engine_api.core.model;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class GameMap {

	private float zoomFactor;
	private List<PlanetInitialization> planets;

}
