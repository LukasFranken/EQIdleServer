package de.instinct.engine_api.core.model;

import com.badlogic.gdx.math.Vector2;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlanetInitialization {
	
	private int ownerId;
	private Vector2 position;
	private boolean ancient;

}

