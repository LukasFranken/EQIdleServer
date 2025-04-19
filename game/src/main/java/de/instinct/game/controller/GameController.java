package de.instinct.game.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseController;
import base.discovery.Discovery;
import base.discovery.dto.ServiceRegistrationDTO;
import base.discovery.impl.RESTDiscovery;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController extends BaseController {
	
	private Discovery discovery = new RESTDiscovery("localhost", 6000);
	
	@Value("${application.version}")
    private String version;
	
	@GetMapping("")
	public ResponseEntity<String> registerService() {
		return ResponseEntity.ok(discovery.register(ServiceRegistrationDTO.builder()
				.serviceTag("game")
				.serviceName("Game Manager Module")
				.serviceUrl("http://eqgame.dev:9011/game")
				.serviceVersion(version)
				.build()).toString());
	}

}
