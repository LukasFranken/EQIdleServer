package de.instinct.eq_meta.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.meta.dto.NameRegisterResponseCode;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.eq_meta.service.UserService;
import de.instinct.eq_meta.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/meta")
public class PlayerMetaController extends BaseServiceController {
	
	private final UserService userService;
	
	public PlayerMetaController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("meta", serverPort, version);
		userService = new UserServiceImpl();
	}
	
	@PostMapping("/register/{username}")
	public ResponseEntity<NameRegisterResponseCode> registerName(@RequestHeader String token, @PathVariable String username) {
		return ResponseEntity.ok(userService.registerName(token, username));
	}
	
	@GetMapping("/profile")
	public ResponseEntity<ProfileData> profile(@RequestHeader String token) {
		return ResponseEntity.ok(userService.getProfile(token));
	}

}
