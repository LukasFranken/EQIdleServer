package de.instinct.api.construction.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlayerInfrastructure {
	
	private float resourceGenerationSpeed;
	private float maxResourceCapacity;
	private List<PlayerTurretData> playerTurrets;

}
