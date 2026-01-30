package de.instinct.api.control.model;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Dto
@Data
@Builder
@AllArgsConstructor
public class Table {
	
	private List<TableHeader> headers;
	private List<TableRow> rows;

}
