package de.instinct.construction.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;
import de.instinct.api.construction.dto.PlayerInfrastructure;
import de.instinct.api.construction.dto.UseTurretResponseCode;
import de.instinct.base.controller.BaseServiceController;
import de.instinct.construction.service.ConstructionService;
import de.instinct.construction.service.impl.ConstructionServiceImpl;

@RestController
@RequestMapping("/construction")
public class ConstructionController extends BaseServiceController {
	
	private final ConstructionService service;
	
	public ConstructionController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("construction", serverPort, version);
		service = new ConstructionServiceImpl();
	}
	
	@Override
	protected void connectToAPIs() {
		
	}
	
	@GetMapping("/init/{token}")
	public ResponseEntity<InfrastructureInitializationResponseCode> init(@PathVariable String token) {
		return ResponseEntity.ok(service.init(token));
	}
	
	@GetMapping("/data/{token}")
	public ResponseEntity<PlayerInfrastructure> data(@PathVariable String token) {
		return ResponseEntity.ok(service.getInfrastructure(token));
	}
	
	@GetMapping("/construction")
	public ResponseEntity<Infrastructure> construction() {
		return ResponseEntity.ok(service.getBaseData());
	}
	
	@PostMapping("/use/{token}/{turretUUID}")
	public ResponseEntity<UseTurretResponseCode> use(@PathVariable String token, @PathVariable String turretUUID) {
		return ResponseEntity.ok(service.useTurret(token, turretUUID));
	}

}
