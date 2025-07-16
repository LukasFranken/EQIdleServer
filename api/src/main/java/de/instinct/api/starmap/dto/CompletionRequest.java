package de.instinct.api.starmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletionRequest {
	
	private String userUUID;
	private int galaxyId;
	private int systemId;

}
