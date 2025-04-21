package de.instinct.eq_auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.auth.dto.TokenVerificationResponse;
import de.instinct.eq_auth.service.AuthenticationService;
import de.instinct.eq_auth.service.impl.AuthenticationServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthenticationController extends BaseServiceController {
	
	private final AuthenticationService authenticationService;
	
	public AuthenticationController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("auth", serverPort, version);
		authenticationService = new AuthenticationServiceImpl();
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
