package de.instinct.api.game.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.game.service.GameInterface;

public class Game extends BaseService implements GameInterface {

	public Game() {
		super("game");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}

	@Override
	public void start() {
		if (!isConnected()) return;
		super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("start")
				.build());
	}

	@Override
	public void stop() {
		if (!isConnected()) return;
		super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("stop")
				.build());
	}

	@Override
	public void create(GameSessionInitializationRequest request) {
		if (!isConnected()) return;
		super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("create")
				.payload(request)
				.build());
	}

}
