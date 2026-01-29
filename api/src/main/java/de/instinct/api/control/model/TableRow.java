package de.instinct.api.control.model;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Dto
@Data
@Builder
@AllArgsConstructor
public class TableRow {
	
	private List<TableCell> cells;
	
	@Default
	private String attributes = "attributes=none";
	
	private String className;

}
