package de.instinct.eq_auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseController;
import base.discovery.Discovery;
import base.discovery.dto.ServiceRegistrationDTO;
import base.discovery.impl.RESTDiscovery;
import de.instinct.eq_auth.controller.dto.TokenVerificationResponse;
import de.instinct.eq_auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController extends BaseController {
	
	private Discovery discovery = new RESTDiscovery("localhost", 6000);
	private final AuthenticationService authenticationService;
	
	@Value("${server.port}")
    private int serverPort;
	
	@Value("${application.version}")
    private String version;
	
	@GetMapping("")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok(discovery.register(ServiceRegistrationDTO.builder()
				.serviceTag("auth")
				.serviceName("Authentication Module")
				.serviceUrl("http://eqgame.dev:" + serverPort + "/auth")
				.serviceVersion(version)
				.build()).toString());
	}
	
	@GetMapping("/verify/{token}")
	public ResponseEntity<TokenVerificationResponse> verify(@PathVariable String token) {
		return ResponseEntity.ok(authenticationService.verify(token));
	}
	
	@GetMapping("/register")
	public ResponseEntity<String> register() {
		return ResponseEntity.ok(authenticationService.register());
	}

}
