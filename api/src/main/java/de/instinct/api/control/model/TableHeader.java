package de.instinct.api.control.model;

import de.instinct.api.core.annotation.Dto;
import lombok.Builder;
import lombok.Data;

@Dto
@Data
@Builder
public class TableHeader {
	
	private String label;
	private String className;

}
