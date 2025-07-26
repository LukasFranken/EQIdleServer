package de.instinct.api.commander.service.impl;

import de.instinct.api.commander.dto.CommanderData;
import de.instinct.api.commander.dto.CommanderInitializationResponseCode;
import de.instinct.api.commander.dto.CommanderRankUpResponseCode;
import de.instinct.api.commander.dto.RankUpCommanderUpgrade;
import de.instinct.api.commander.service.CommanderInterface;
import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.meta.dto.PlayerRank;

public class Commander extends BaseService implements CommanderInterface {

	public Commander() {
		super("commander");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}
	
	@Override
	public CommanderInitializationResponseCode init(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("init")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, CommanderInitializationResponseCode.class);
	}
	
	@Override
	public CommanderData data(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("data")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, CommanderData.class);
	}

	@Override
	public CommanderRankUpResponseCode rankup(String token, PlayerRank rank) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("rankup")
				.pathVariable(token)
				.payload(rank)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, CommanderRankUpResponseCode.class);
	}
	
	@Override
	public RankUpCommanderUpgrade upgrade(PlayerRank rank) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("rankup")
				.payload(rank)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, RankUpCommanderUpgrade.class);
	}
	
}
