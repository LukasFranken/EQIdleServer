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

import base.controller.BaseController;
import base.discovery.Discovery;
import base.discovery.dto.ServiceRegistrationDTO;
import base.discovery.impl.RESTDiscovery;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationRequest;
import de.instinct.matchmaker.controller.dto.MatchmakingRegistrationResponse;
import de.instinct.matchmaker.controller.dto.MatchmakingStatusResponse;
import de.instinct.matchmaker.service.MatchmakingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matchmaking")
public class PlayerMatchmakingController extends BaseController {
	
	private Discovery discovery = new RESTDiscovery("localhost", 6000);
	
	private final MatchmakingService matchmakingService;
	
	@Value("${application.version}")
    private String version;
	
	@GetMapping("")
	public ResponseEntity<String> registerService() {
		return ResponseEntity.ok(discovery.register(ServiceRegistrationDTO.builder()
				.serviceTag("matchmaking")
				.serviceName("Matchmaking Module")
				.serviceUrl("http://eqgame.dev:9010/matchamking")
				.serviceVersion(version)
				.build()).toString());
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
