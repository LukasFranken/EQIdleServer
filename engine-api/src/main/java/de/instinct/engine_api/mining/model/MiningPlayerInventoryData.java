package de.instinct.engine_api.mining.model;

import java.util.Map;

import de.instinct.api.core.annotation.Dto;
import de.instinct.engine.mining.entity.asteroid.ResourceType;
import lombok.Data;

@Dto
@Data
public class MiningPlayerInventoryData {
	
	private Map<ResourceType, Float> resources;

}
