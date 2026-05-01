package de.instinct.api.mining.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class CreateSessionRequest {
	
	private String map;
	private List<String> playerUUIDs;

}
