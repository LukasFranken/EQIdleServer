package de.instinct.mining.service.model;

import com.badlogic.gdx.math.Vector2;

import de.instinct.api.core.annotation.Dto;
import de.instinct.engine.mining.entity.asteroid.ResourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class AsteroidMapData {
	
	private Vector2 position;
	private float radius;
	public float health;
	public float value;
	public ResourceType resourceType;

}
