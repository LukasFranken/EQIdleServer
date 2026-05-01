package de.instinct.mining.service.model;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class MiningMapData {
	
	private Vector2 recallPosition;
	private float recallRadius;
	private List<AsteroidMapData> asteroids;

}
