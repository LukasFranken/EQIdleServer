package de.instinct.api.control.model;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Dto
@Data
@Builder
public class TableRow {
	
	private List<TableCell> cells;
	
	@Default
	private String attributes = "attributes=none";
	
	private String className;

}
