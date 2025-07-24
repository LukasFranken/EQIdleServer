package de.instinct.api.starmap.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class CompletionRequest {
	
	private String userUUID;
	private int galaxyId;
	private int systemId;

}
