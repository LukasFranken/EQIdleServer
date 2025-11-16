package de.instinct.api.shipyard.dto.admin;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShipCreateRequest {
	
	private String name;
	private String type;

}
