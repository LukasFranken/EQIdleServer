package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ShipStatisticReportResponse;
import lombok.Data;

@Dto
@Data
public class PlayerShipResult {
	
	private ShipStatisticReportResponse responseCode;
	private String uuid;
	private List<ShipResult> shipResults;

}
