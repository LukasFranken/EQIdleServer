package de.instinct.engine_api.mining.service.model;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.engine.mining.entity.asteroid.ResourceType;
import lombok.Data;

@Dto
@Data
public class MiningMissionOverview {
	
	private String name;
	private int asteroids;
	private List<ResourceType> availableResources;

}
