package de.instinct.api.game.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PreviewPlanet {
	
	private float xPos;
	private float yPos;
	private int ownerId;
	private boolean isAncient;

}
