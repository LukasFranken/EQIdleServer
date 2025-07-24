package de.instinct.api.starmap.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class SystemCompletionData {
	
	private int galaxyId;
	private int systemId;
	private int completedTimes;
	private long firstCompletedAt;
	private long lastCompletedAt;

}
