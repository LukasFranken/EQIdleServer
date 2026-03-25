package de.instinct.api.starmap.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class StartConquestRequest {
	
	private String groupToken;
	private String userToken;
	private int galaxyId;
	private int systemId;

}
