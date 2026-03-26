package de.instinct.api.starmap.dto;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.matchmaking.model.FactionMode;
import lombok.Data;

@Dto
@Data
public class SectorDataRequest {
	
	private SectorDataRequestType type;
	private FactionMode mode;
	private String token;
	private String groupToken;

}
