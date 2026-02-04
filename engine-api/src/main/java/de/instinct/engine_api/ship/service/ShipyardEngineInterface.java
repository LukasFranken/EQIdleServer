package de.instinct.engine_api.ship.service;

import de.instinct.api.matchmaking.dto.PlayerShipResult;
import de.instinct.api.shipyard.service.ShipyardInterface;
import de.instinct.engine_api.ship.model.ShipStatisticReportRequest;

public interface ShipyardEngineInterface extends ShipyardInterface {
	
	PlayerShipResult statistic(ShipStatisticReportRequest request);

}
