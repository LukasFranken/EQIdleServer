package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShipResult {

	private String model;
	private List<ShipComponentResult> componentResults;
	
}
