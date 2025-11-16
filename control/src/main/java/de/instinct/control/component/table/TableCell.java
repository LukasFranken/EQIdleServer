package de.instinct.control.component.table;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableCell {
	
	private String value;
	private Map<String, String> attributes;
	private String attributestring;
	private boolean hasattributes;
	private String test;

}
