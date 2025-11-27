package de.instinct.api.shipyard.dto.admin;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ComponentDeleteRequest {
	
	private String name;
	private int id;

}
