package de.instinct.api.shipyard.dto.ship;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public abstract class ShipComponent {
	
	private int id;
	private String description;

}
