package de.instinct.control.component.table;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
public class TableRow {
	
	private List<TableCell> cells;
	
	@Default
	private String attributes = "attributes=none";
	
	private String className;

}
