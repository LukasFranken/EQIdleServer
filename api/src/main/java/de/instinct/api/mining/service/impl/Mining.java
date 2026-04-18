package de.instinct.api.mining.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.mining.service.MiningInterface;

public class Mining extends BaseService implements MiningInterface {

	public Mining() {
		super("mining");
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

}
