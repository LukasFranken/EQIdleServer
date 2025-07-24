package de.instinct.api.construction.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlanetDefense {
	
	private float shieldRegenerationSpeed;
	private float shield;
	private float armor;

}
