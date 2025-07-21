package de.instinct.api.shipyard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerShipData {
	
	private String uuid;
	private int shipId;
	private int level;
	private boolean built;
	private boolean inUse;

}