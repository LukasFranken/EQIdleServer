package de.instinct.discovery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseController;
import de.instinct.api.discovery.dto.ServiceInfoDTO;
import de.instinct.discovery.service.DiscoveryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discovery")
public class DiscoveryController extends BaseController {
	
	private final DiscoveryService discoverySerivce;
	
	@GetMapping("/single/{serviceTag}")
	public ResponseEntity<ServiceInfoDTO> discover(@PathVariable String serviceTag) {
		return ResponseEntity.ok(discoverySerivce.discover(serviceTag)); 
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<ServiceInfoDTO>> discoverAll() {
		return ResponseEntity.ok(discoverySerivce.discoverAll()); 
	}

}
