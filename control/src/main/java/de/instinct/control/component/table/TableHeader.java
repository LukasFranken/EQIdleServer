package de.instinct.control.component.table;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableHeader {
	
	private String label;
	private String className;

}
