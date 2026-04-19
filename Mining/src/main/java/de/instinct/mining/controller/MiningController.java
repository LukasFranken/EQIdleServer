package de.instinct.mining.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.api.core.config.APIConfiguration;
import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;
import de.instinct.base.controller.BaseServiceController;
import de.instinct.engine_api.core.EngineAPI;
import de.instinct.mining.config.ApplicationConfiguration;
import de.instinct.mining.service.MiningService;

@RestController
@RequestMapping("/mining")
public class MiningController extends BaseServiceController {

	private final MiningService service;
	
	@Autowired
	public MiningController(ApplicationConfiguration config) {
	    super("social", config.getPort(), config.getVersion());
	    this.service = new MiningService(config);
	}
	
	@Override
	protected void connectToAPIs() {
		EngineAPI.initialize(APIConfiguration.SERVER);
		EngineAPI.matchmaking().connect();
		EngineAPI.meta().connect();
		start();
	}
	
	@GetMapping("/start")
	public ResponseEntity<?> start() {
		service.start();
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/create")
	public ResponseEntity<CreateSessionResponse> createSession(@RequestBody CreateSessionRequest request) {
		return ResponseEntity.ok(service.createSession(request));
	}
	
}
