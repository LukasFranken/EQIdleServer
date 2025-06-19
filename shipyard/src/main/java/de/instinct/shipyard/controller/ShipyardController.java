package de.instinct.shipyard.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;
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
	
	@GetMapping("/init/{token}")
	public ResponseEntity<ShipyardInitializationResponseCode> init(@PathVariable String token) {
		return ResponseEntity.ok(service.init(token));
	}
	
	@GetMapping("/data/{token}")
	public ResponseEntity<ShipyardData> data(@PathVariable String token) {
		return ResponseEntity.ok(service.getShipyardData(token));
	}
	
	@PostMapping("/use/{token}/{shipUUID}")
	public ResponseEntity<UseShipResponseCode> use(@PathVariable String token, @PathVariable String shipUUID) {
		return ResponseEntity.ok(service.useShip(token, shipUUID));
	}
	
	@PostMapping("/unuse/{token}/{shipUUID}")
	public ResponseEntity<UnuseShipResponseCode> unuse(@PathVariable String token, @PathVariable String shipUUID) {
		return ResponseEntity.ok(service.unuseShip(token, shipUUID));
	}

}
