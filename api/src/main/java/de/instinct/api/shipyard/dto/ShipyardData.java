package de.instinct.api.shipyard.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShipyardData {
	
	private int baseSlots;
	private int baseActiveShipSlots;
	private List<ShipBlueprint> shipBlueprints;

}
