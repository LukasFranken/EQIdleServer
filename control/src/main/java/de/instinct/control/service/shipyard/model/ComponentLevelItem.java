package de.instinct.control.service.shipyard.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComponentLevelItem {
	
	private int level;
	private String requirementType;
	private float requirementValue;

}
