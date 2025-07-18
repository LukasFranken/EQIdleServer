package de.instinct.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.core.API;
import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.game.dto.MapPreview;
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
	
	@Override
	protected void connectToAPIs() {
		API.matchmaking().connect();
		API.meta().connect();
		start();
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
	public ResponseEntity<String> createSession(@RequestBody GameSessionInitializationRequest request) {
		return ResponseEntity.ok(service.createSession(request));
	}
	
	@GetMapping("/preview/{map}")
	public ResponseEntity<MapPreview> preview(@PathVariable("map") String map) {;
		return ResponseEntity.ok(service.preview(map));
	}

}
