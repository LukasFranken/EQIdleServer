package de.instinct.api.starmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemCompletionData {
	
	private int galaxyId;
	private int systemId;
	private int completedTimes;
	private long firstCompletedAt;
	private long lastCompletedAt;

}
