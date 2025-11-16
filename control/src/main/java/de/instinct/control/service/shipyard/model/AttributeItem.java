package de.instinct.control.service.shipyard.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttributeItem {
	
	private String name;
	private float value;

}
