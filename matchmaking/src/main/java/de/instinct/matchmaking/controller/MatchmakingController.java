package de.instinct.matchmaking.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.core.model.GeneralRequestResponse;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationRequest;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponse;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.matchmaking.service.MatchmakingService;
import de.instinct.matchmaking.service.impl.MatchmakingServiceImpl;

@RestController
@RequestMapping("/matchmaking")
public class MatchmakingController extends BaseServiceController {
	
	private final MatchmakingService matchmakingService;
	
	public MatchmakingController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
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
	
	@PutMapping("/callback/{lobbyUUID}")
	public ResponseEntity<GeneralRequestResponse> callback(@PathVariable String lobbyUUID, @RequestBody CallbackCode code) {
		matchmakingService.callback(lobbyUUID, code);
		return ResponseEntity.ok(GeneralRequestResponse.OK);
	}
	
	@PutMapping("/dispose/{lobbyUUID}")
	public ResponseEntity<GeneralRequestResponse> dispose(@PathVariable String lobbyUUID) {
		matchmakingService.dispose(lobbyUUID);
		return ResponseEntity.ok(GeneralRequestResponse.OK);
	}
	
}
