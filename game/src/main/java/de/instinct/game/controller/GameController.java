package de.instinct.game.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.game.dto.GameserverInitializationRequest;
import de.instinct.game.service.GameserverManagerService;
import de.instinct.game.service.impl.GameserverManagerServiceImpl;

@RestController
@RequestMapping("/game")
public class GameController extends BaseServiceController {
	
	private final GameserverManagerService service;
	
	public GameController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("game", serverPort, version);
		service = new GameserverManagerServiceImpl();
	}
	
	@PostMapping("/start")
	public ResponseEntity<?> start(@RequestBody GameserverInitializationRequest request) {
		service.start(request);
		return ResponseEntity.ok().build();
	}

}
