package de.instinct.api.shipyard.dto.admin.component;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ComponentDeleteRequest {
	
	private String shipname;
	private int id;

}
