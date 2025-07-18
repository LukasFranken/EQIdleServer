package de.instinct.api.game.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreviewPlanet {
	
	private float xPos;
	private float yPos;
	private int ownerId;
	private boolean isAncient;

}
