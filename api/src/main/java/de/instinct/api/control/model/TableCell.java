package de.instinct.api.control.model;

import de.instinct.api.core.annotation.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Dto
@Data
@Builder
@AllArgsConstructor
public class TableCell {
	
	private String value;
	
	@Default
	private String attributes = "attributes=none";
	
	private String className;

}
