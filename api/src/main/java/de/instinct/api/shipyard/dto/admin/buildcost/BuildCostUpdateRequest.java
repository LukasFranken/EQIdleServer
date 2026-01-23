package de.instinct.api.shipyard.dto.admin.buildcost;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class BuildCostUpdateRequest {
	
	private String shipname;
	private int id;
	private String resourceType;
	private long cost;

}
