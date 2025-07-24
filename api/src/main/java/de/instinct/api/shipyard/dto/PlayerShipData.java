package de.instinct.api.shipyard.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlayerShipData {
	
	private String uuid;
	private int shipId;
	private int level;
	private boolean built;
	private boolean inUse;

}