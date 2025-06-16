package de.instinct.meta.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import de.instinct.api.core.API;
import de.instinct.api.meta.dto.ExperienceUpdateResponseCode;
import de.instinct.api.meta.dto.LoadoutData;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.meta.dto.ResourceUpdateResponseCode;
import de.instinct.meta.service.UserService;

@RestController
@RequestMapping("/meta")
public class MetaController extends BaseServiceController {
	
	private final UserService userService;

	@Autowired
	public MetaController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version, UserService userService) {
	    super("meta", serverPort, version);
	    this.userService = userService;
	}
	
	@Override
	protected void connectToAPIs() {
		API.shipyard().connect();
		API.construction().connect();
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
	public ResponseEntity<LoadoutData> loadout(@PathVariable String token) {
		return ResponseEntity.ok(userService.getLoadout(token));
	}
	
	@PostMapping("/initialize/{token}")
	public ResponseEntity<RegisterResponseCode> initialize(@PathVariable String token) {
		return ResponseEntity.ok(userService.initialize(token));
	}
	
	@GetMapping("/token/{username}")
	public ResponseEntity<String> token(@PathVariable String username) {
		return ResponseEntity.ok(userService.token(username));
	}
	
	@GetMapping("/resources/{token}")
	public ResponseEntity<ResourceData> resources(@PathVariable String token) {
		return ResponseEntity.ok(userService.getResources(token));
	}
	
	@PostMapping("/resources/{token}")
	public ResponseEntity<ResourceUpdateResponseCode> addResources(@PathVariable String token, @RequestBody ResourceData resourceUpdate) {
		return ResponseEntity.ok(userService.updateResources(token, resourceUpdate));
	}
	
	@PostMapping("/experience/{token}/{experience}")
	public ResponseEntity<ExperienceUpdateResponseCode> experience(@PathVariable String token, @PathVariable String experience) {
		return ResponseEntity.ok(userService.addExperience(token, experience));
	}
	
}
