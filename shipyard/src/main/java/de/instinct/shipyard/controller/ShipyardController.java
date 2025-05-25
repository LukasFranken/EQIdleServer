package de.instinct.shipyard.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.shipyard.service.ShipyardService;
import de.instinct.shipyard.service.impl.ShipyardServiceImpl;

@RestController
@RequestMapping("/shipyard")
public class ShipyardController extends BaseServiceController {
	
	private final ShipyardService service;

	public ShipyardController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("shipyard", serverPort, version);
		service = new ShipyardServiceImpl();
	}
	
	@Override
	protected void connectToAPIs() {
		
	}

}
