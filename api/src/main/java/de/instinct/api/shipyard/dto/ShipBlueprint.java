package de.instinct.api.shipyard.dto;

import java.util.List;

import de.instinct.api.meta.dto.ResourceAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipBlueprint {
	
	private String uuid;
	private ShipType type;
	private String model;
	private float movementSpeed;
	private int cost;
	private int commandPointsCost;
	private ShipWeapon weapon;
	private ShipDefense defense;
	
	private List<ResourceAmount> buildCost;
	private boolean built;
	private List<ShipLevel> levels;
	
	private boolean inUse;

}
