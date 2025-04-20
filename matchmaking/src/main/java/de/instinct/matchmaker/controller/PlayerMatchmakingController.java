package de.instinct.matchmaker.controller;

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
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponse;
import de.instinct.matchmaker.service.MatchmakingService;
import de.instinct.matchmaker.service.impl.MatchmakingServiceImpl;

@RestController
@RequestMapping("/matchmaking")
public class PlayerMatchmakingController extends BaseServiceController {
	
	private final MatchmakingService matchmakingService;
	
	public PlayerMatchmakingController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("matchmaking", serverPort, version);
		matchmakingService = new MatchmakingServiceImpl();
	}
	
	@PostMapping("/register")
	public ResponseEntity<MatchmakingRegistrationResponse> register(@RequestHeader("token") String authToken, @RequestBody MatchmakingRegistrationRequest request) {
		return ResponseEntity.ok(matchmakingService.register(authToken, request));
	}
	
	@GetMapping("/status/{lobbyUUID}")
	public ResponseEntity<MatchmakingStatusResponse> status(@PathVariable("lobbyUUID") String lobbyUUID) {
		return ResponseEntity.ok(matchmakingService.getStatus(lobbyUUID));
	}
	
}
