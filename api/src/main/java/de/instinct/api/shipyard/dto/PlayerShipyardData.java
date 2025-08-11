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
	
	public int getUsedSlots() {
		int usedSlots = 0;
		for (PlayerShipData ship : ships) {
			if (ship.isBuilt()) {
				usedSlots++;
			}
		}
		return usedSlots;
	}

}
