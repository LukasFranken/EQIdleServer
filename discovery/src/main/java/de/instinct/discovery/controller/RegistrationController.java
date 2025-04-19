package de.instinct.discovery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.discovery.dto.RegistrationResponseCode;
import base.discovery.dto.ServiceRegistrationDTO;
import de.instinct.discovery.service.DiscoveryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
	
	private final DiscoveryService discoverySerivce;
	
	@PostMapping("")
	public ResponseEntity<RegistrationResponseCode> register(@RequestBody ServiceRegistrationDTO serviceRegistrationDTO) {
		return ResponseEntity.ok(discoverySerivce.registerService(serviceRegistrationDTO));
	}
	
	@GetMapping("/pingalive/{serviceTag}")
	public ResponseEntity<String> pingalive(@PathVariable String serviceTag) {
		return ResponseEntity.ok(discoverySerivce.pingalive(serviceTag));
	}
	
}
