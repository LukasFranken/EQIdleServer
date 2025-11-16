package de.instinct.api.shipyard.dto.ship;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.meta.dto.ResourceAmount;
import lombok.Data;

@Dto
@Data
public class ShipBlueprint {
	
	private int id;
	private String model;
	private long created;
	private long lastModified;
	private List<ShipComponent> components;
	private List<ResourceAmount> buildCost;

}
