package de.instinct.control.component.table;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableRow {
	
	private List<TableCell> cells;

}
