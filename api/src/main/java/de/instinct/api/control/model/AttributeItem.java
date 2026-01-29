package de.instinct.api.control.model;

import de.instinct.api.core.annotation.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Dto
@Data
@Builder
@AllArgsConstructor
public class AttributeItem {
	
	private String name;
	private float value;

}
