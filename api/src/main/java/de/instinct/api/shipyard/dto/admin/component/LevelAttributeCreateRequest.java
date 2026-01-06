package de.instinct.api.shipyard.dto.admin.component;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class LevelAttributeCreateRequest {
	
	private String shipname;
	private int componentId;
	private int level;

}
