package de.instinct.engine_api.ship.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.matchmaking.dto.PlayerShipResult;
import de.instinct.api.shipyard.service.impl.Shipyard;
import de.instinct.engine_api.ship.model.ShipStatisticReportRequest;
import de.instinct.engine_api.ship.service.ShipyardEngineInterface;

public class ShipyardEngine extends Shipyard implements ShipyardEngineInterface {

	@Override
	public PlayerShipResult statistic(ShipStatisticReportRequest request) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("statistic")
				.payload(request)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, PlayerShipResult.class);
	}

}
