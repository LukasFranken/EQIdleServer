package de.instinct.api.shipyard.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.meta.dto.ResourceAmount;
import lombok.Data;

@Dto
@Data
public class ShipBlueprint {
	
	private int id;
	private ShipType type;
	private String model;
	private float movementSpeed;
	private int cost;
	private int commandPointsCost;
	private ShipWeapon weapon;
	private ShipDefense defense;
	private List<ResourceAmount> buildCost;
	private List<ShipLevel> levels;

}
