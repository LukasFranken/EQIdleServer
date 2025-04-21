package de.instinct.eq_meta.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.api.meta.dto.RegisterResponseCode;
import de.instinct.eq_meta.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meta")
public class ServerMetaController {
	
	private final UserService userService;
	
	@PostMapping("/initialize/{token}")
	public ResponseEntity<RegisterResponseCode> initialize(@PathVariable String token) {
		return ResponseEntity.ok(userService.initialize(token));
	}

}
