package de.instinct.engine_api.ship.model;

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
