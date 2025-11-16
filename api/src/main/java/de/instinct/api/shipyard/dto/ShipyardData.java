package de.instinct.api.shipyard.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.ShipBlueprint;
import lombok.Data;

@Dto
@Data
public class ShipyardData {
	
	private int baseSlots;
	private int baseActiveShipSlots;
	private int currentShipId;
	private List<ShipBlueprint> shipBlueprints;

}
