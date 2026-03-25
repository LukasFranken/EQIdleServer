package de.instinct.api.starmap.dto;

import java.util.List;
import java.util.Map;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.matchmaking.model.FactionMode;
import lombok.Data;

@Dto
@Data
public class PlayerStarmapData {
	
	private Map<FactionMode, List<SystemCompletionData>> completedSystems;

}
