package de.instinct.api.shipyard.dto;

import java.util.List;

import de.instinct.api.meta.dto.ShipData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipyardData {
	
	private int slots;
	private List<ShipData> ownedShips;

}
