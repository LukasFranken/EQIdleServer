package de.instinct.matchmaking.service.model;

import java.util.List;

import de.instinct.api.meta.dto.ResourceAmount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameRewards {
	
	private long experience;
	private List<ResourceAmount> resources;

}
