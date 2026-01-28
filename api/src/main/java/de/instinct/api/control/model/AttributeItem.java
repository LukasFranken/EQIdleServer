package de.instinct.api.control.model;

import de.instinct.api.core.annotation.Dto;
import lombok.Builder;
import lombok.Data;

@Dto
@Data
@Builder
public class AttributeItem {
	
	private String name;
	private float value;

}
