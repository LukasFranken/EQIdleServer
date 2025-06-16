package de.instinct.meta.controller.sub;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.api.core.modules.MenuModule;
import de.instinct.api.meta.dto.modules.ModuleData;
import de.instinct.api.meta.dto.modules.ModuleInfoRequest;
import de.instinct.api.meta.dto.modules.ModuleInfoResponse;
import de.instinct.api.meta.dto.modules.ModuleRegistrationResponseCode;
import de.instinct.meta.service.ModuleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/meta/module")
@RequiredArgsConstructor
public class ModuleController {
	
	private final ModuleService moduleService;
	
	@GetMapping("/{token}")
	public ResponseEntity<ModuleData> modules(@PathVariable String token) {
		return ResponseEntity.ok(moduleService.getModules(token));
	}
	
	@PostMapping("/{token}")
	public ResponseEntity<ModuleRegistrationResponseCode> registerModule(@PathVariable String token, @RequestBody MenuModule module) {
		return ResponseEntity.ok(moduleService.registerModule(token, module));
	}
	
	@PostMapping("/info")
	public ResponseEntity<ModuleInfoResponse> info(@RequestBody ModuleInfoRequest moduleInfoRequest) {
		return ResponseEntity.ok(moduleService.getInfo(moduleInfoRequest));
	}

}
