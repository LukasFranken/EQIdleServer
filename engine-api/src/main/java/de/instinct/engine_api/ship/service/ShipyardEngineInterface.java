package de.instinct.engine_api.ship.service;

import de.instinct.api.shipyard.service.ShipyardInterface;
import de.instinct.engine_api.ship.model.ShipStatisticReportRequest;
import de.instinct.engine_api.ship.model.ShipStatisticReportResponse;

public interface ShipyardEngineInterface extends ShipyardInterface {
	
	ShipStatisticReportResponse statistic(ShipStatisticReportRequest request);

}
