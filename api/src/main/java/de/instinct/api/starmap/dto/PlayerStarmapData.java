package de.instinct.api.starmap.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlayerStarmapData {
	
	private List<SystemCompletionData> completedSystems;
	private SectorData sectorData;

}
