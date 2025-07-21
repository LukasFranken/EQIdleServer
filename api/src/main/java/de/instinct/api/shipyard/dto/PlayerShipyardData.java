package de.instinct.api.shipyard.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerShipyardData {
	
	private int slots;
	private int activeShipSlots;
	private List<PlayerShipData> ships;

}
