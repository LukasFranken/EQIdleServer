package de.instinct.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.game.config.ApplicationConfig;
import de.instinct.game.config.GameserverConfig;
import de.instinct.game.service.GameserverManagerService;
import de.instinct.game.service.impl.GameserverManagerServiceImpl;

@RestController
@RequestMapping("/game")
public class GameController extends BaseServiceController {
	
	private final ApplicationConfig applicationCofig;
	private final GameserverManagerService service;
	
	public GameController(ApplicationConfig applicationCofig, GameserverConfig gameserverConfig) {
		super("game", applicationCofig.getPort(), applicationCofig.getVersion());
		service = new GameserverManagerServiceImpl(gameserverConfig);
		this.applicationCofig = applicationCofig;
	}
	
	@GetMapping()
	public ResponseEntity<String> getVersion() {
		return ResponseEntity.ok("v" + applicationCofig.getVersion());
	}
	
	@GetMapping("/start")
	public ResponseEntity<?> start() {
		service.start();
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/stop")
	public ResponseEntity<?> stop() {
		service.stop();
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createSession(@RequestBody GameSessionInitializationRequest request) {
		service.createSession(request);
		return ResponseEntity.ok().build();
	}

}
