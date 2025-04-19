package de.instinct.eq_meta.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseController;
import base.discovery.Discovery;
import base.discovery.dto.ServiceRegistrationDTO;
import base.discovery.impl.RESTDiscovery;
import de.instinct.eq_meta.controller.dto.NameRegisterResponseCode;
import de.instinct.eq_meta.service.UserService;
import de.instinct.eq_meta.service.model.ProfileData;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meta")
public class PlayerMetaController extends BaseController {
	
	private Discovery discovery = new RESTDiscovery("localhost", 6000);
	private final UserService userService;
	
	@Value("${application.version}")
    private String version;
	
	@GetMapping("")
	public ResponseEntity<String> registerService() {
		return ResponseEntity.ok(discovery.register(ServiceRegistrationDTO.builder()
				.serviceTag("meta")
				.serviceName("Meta Module")
				.serviceUrl("http://eqgame.dev:9008/meta")
				.serviceVersion(version)
				.build()).toString());
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
