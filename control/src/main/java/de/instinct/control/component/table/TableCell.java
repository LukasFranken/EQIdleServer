package de.instinct.control.component.table;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class TableCell {
	
	private String value;
	
	@Default
	private String attributes = "attributes=none";
	
	private String className;

}
