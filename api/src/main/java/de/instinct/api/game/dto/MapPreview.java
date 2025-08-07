package de.instinct.api.game.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class MapPreview {
	
	private float zoomFactor;
	private List<PreviewPlanet> planets;

}
