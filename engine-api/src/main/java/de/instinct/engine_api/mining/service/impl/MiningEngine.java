package de.instinct.engine_api.mining.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.mining.service.impl.Mining;
import de.instinct.engine_api.mining.model.MiningPlayerInventoryData;
import de.instinct.engine_api.mining.service.MiningEngineInterface;
import de.instinct.engine_api.mining.service.model.MiningMissionOverview;

public class MiningEngine extends Mining implements MiningEngineInterface {

	@Override
	public MiningPlayerInventoryData inventory(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("inventory")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, MiningPlayerInventoryData.class);
	}
	
	@Override
	public MiningMissionOverview mission(String missionName) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("mission")
				.pathVariable(missionName)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, MiningMissionOverview.class);
	}

}
