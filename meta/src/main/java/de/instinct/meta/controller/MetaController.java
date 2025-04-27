package de.instinct.meta.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.meta.dto.Loadout;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.meta.service.UserService;
import de.instinct.meta.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/meta")
public class MetaController extends BaseServiceController {
	
	private final UserService userService;
	
	public MetaController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("meta", serverPort, version);
		userService = new UserServiceImpl();
	}
	
	@Override
	protected void connectToAPIs() {
		
	}
	
	@PostMapping("/register/{username}")
	public ResponseEntity<NameRegisterResponseCode> registerName(@RequestHeader String token, @PathVariable String username) {
		return ResponseEntity.ok(userService.registerName(token, username));
	}
	
	@GetMapping("/profile/{token}")
	public ResponseEntity<ProfileData> profile(@PathVariable String token) {
		return ResponseEntity.ok(userService.getProfile(token));
	}
	
	@GetMapping("/loadout/{token}")
	public ResponseEntity<Loadout> loadout(@PathVariable String token) {
		return ResponseEntity.ok(userService.getLoadout(token));
	}
	
	@PostMapping("/initialize/{token}")
	public ResponseEntity<RegisterResponseCode> initialize(@PathVariable String token) {
		return ResponseEntity.ok(userService.initialize(token));
	}

}
