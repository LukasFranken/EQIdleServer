package de.instinct.api.shipyard.dto.ship;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.engine.stats.model.unit.ShipStatistic;
import lombok.Data;

@Dto
@Data
public class ShipStatisticReportRequest {
	
	private String userUUID;
	private List<ShipStatistic> shipStatistics;

}
