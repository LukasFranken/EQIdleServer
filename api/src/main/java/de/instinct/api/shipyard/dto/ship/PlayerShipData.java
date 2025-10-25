package de.instinct.api.shipyard.dto.ship;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlayerShipData {
	
	private String uuid;
	private int shipId;
	private boolean built;
	private boolean inUse;
	private List<PlayerShipComponentLevel> componentLevels;

}