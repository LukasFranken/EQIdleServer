package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.meta.dto.ResourceAmount;
import lombok.Data;

@Dto
@Data
public class PlayerReward {
	
	private String uuid;
	private long experience;
	private List<ResourceAmount> resources;

}
