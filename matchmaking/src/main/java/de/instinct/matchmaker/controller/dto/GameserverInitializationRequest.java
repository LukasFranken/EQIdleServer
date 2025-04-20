package de.instinct.matchmaker.controller.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameserverInitializationRequest {
	
	private List<String> playerUUIDs;

}
