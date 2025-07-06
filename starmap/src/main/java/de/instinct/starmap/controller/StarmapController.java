package de.instinct.starmap.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.core.API;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;
import de.instinct.starmap.service.StarmapService;
import de.instinct.starmap.service.impl.StarmapServiceImpl;

@RestController
@RequestMapping("/starmap")
public class StarmapController extends BaseServiceController {
	
	private final StarmapService service;

	public StarmapController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("starmap", serverPort, version);
		service = new StarmapServiceImpl();
	}
	
	@Override
	protected void connectToAPIs() {
		API.meta().connect();
	}
	
	@GetMapping("/init/{token}")
	public ResponseEntity<StarmapInitializationResponseCode> init(@PathVariable String token) {
		return ResponseEntity.ok(service.init(token));
	}
	
	@GetMapping("/data/{token}")
	public ResponseEntity<SectorData> data(@PathVariable String token) {
		return ResponseEntity.ok(service.getStarmapData(token));
	}

}
