package de.instinct.starmap.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.api.core.API;
import de.instinct.api.matchmaking.model.FactionMode;
import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.CompletionResponse;
import de.instinct.api.starmap.dto.PlayerStarmapData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;
import de.instinct.api.starmap.dto.StartConquestRequest;
import de.instinct.api.starmap.dto.StartConquestResponse;
import de.instinct.api.starmap.service.StarmapInterface;
import de.instinct.base.controller.BaseServiceController;
import de.instinct.starmap.service.impl.StarmapServiceImpl;

@RestController
@RequestMapping("/starmap")
public class StarmapController extends BaseServiceController {
	
	private final StarmapInterface service;

	public StarmapController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("starmap", serverPort, version);
		service = new StarmapServiceImpl();
	}
	
	@Override
	protected void connectToAPIs() {
		API.meta().connect();
		API.game().connect();
		API.social().connect();
		API.matchmaking().connect();
	}
	
	@GetMapping("/init/{token}")
	public ResponseEntity<StarmapInitializationResponseCode> init(@PathVariable String token) {
		return ResponseEntity.ok(service.init(token));
	}
	
	@GetMapping("/load")
	public ResponseEntity<String> load() {
		return ResponseEntity.ok(service.load());
	}
	
	@GetMapping("/data/{token}")
	public ResponseEntity<PlayerStarmapData> data(@PathVariable String token) {
		return ResponseEntity.ok(service.data(token));
	}
	
	@GetMapping("/sector/{mode}")
	public ResponseEntity<SectorData> sector(@PathVariable FactionMode mode) {
		return ResponseEntity.ok(service.sector(mode));
	}
	
	@PostMapping("/start")
	public ResponseEntity<StartConquestResponse> startConquest(@RequestBody StartConquestRequest request) {
		return ResponseEntity.ok(service.start(request));
	}
	
	@PostMapping("/complete")
	public ResponseEntity<CompletionResponse> complete(@RequestBody CompletionRequest request) {
		return ResponseEntity.ok(service.complete(request));
	}

}
