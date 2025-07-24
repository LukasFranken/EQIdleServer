package de.instinct.api.shipyard.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlayerShipyardData {
	
	private int slots;
	private int activeShipSlots;
	private List<PlayerShipData> ships;

}
