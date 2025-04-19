package de.instinct.eq_meta.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.eq_meta.controller.dto.RegisterResponseCode;
import de.instinct.eq_meta.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meta")
public class ServerMetaController {
	
	private final UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponseCode> register(@RequestHeader String token) {
		return ResponseEntity.ok(userService.register(token));
	}

}
