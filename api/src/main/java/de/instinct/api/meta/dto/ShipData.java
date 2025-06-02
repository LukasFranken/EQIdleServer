package de.instinct.api.meta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipData {
	
	private String uuid;
	private ShipType type;
	private String model;
	private float movementSpeed;
	private int cost;
	private int health;
	private int power;
	private boolean inUse;

}
