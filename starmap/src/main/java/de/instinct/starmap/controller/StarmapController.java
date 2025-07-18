package de.instinct.starmap.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.core.API;
import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.CompletionResponse;
import de.instinct.api.starmap.dto.PlayerStarmapData;
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
		API.game().connect();
	}
	
	@GetMapping("/init/{token}")
	public ResponseEntity<StarmapInitializationResponseCode> init(@PathVariable String token) {
		return ResponseEntity.ok(service.init(token));
	}
	
	@GetMapping("/data")
	public ResponseEntity<PlayerStarmapData> data(@RequestHeader String token) {
		return ResponseEntity.ok(service.getStarmapData(token));
	}
	
	@GetMapping("/sector")
	public ResponseEntity<SectorData> sector() {
		return ResponseEntity.ok(service.getSectorData());
	}
	
	@PostMapping("/complete")
	public ResponseEntity<CompletionResponse> complete(@RequestBody CompletionRequest request) {
		return ResponseEntity.ok(service.completeSystem(request));
	}

}
