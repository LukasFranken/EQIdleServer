package de.instinct.mining.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.api.core.config.APIConfiguration;
import de.instinct.base.controller.BaseServiceController;
import de.instinct.engine_api.core.EngineAPI;
import de.instinct.mining.service.MiningService;

@RestController
@RequestMapping("/mining")
public class MiningController extends BaseServiceController {

	private final MiningService service;
	
	@Autowired
	public MiningController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version, MiningService miningService) {
	    super("social", serverPort, version);
	    this.service = miningService;
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
	
}
