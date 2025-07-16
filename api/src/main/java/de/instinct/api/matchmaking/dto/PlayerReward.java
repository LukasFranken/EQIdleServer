package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.meta.dto.ResourceAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerReward {
	
	private String uuid;
	private long experience;
	private List<ResourceAmount> resources;

}
