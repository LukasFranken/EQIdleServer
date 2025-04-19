package de.instinct.matchmaker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.matchmaker.controller.dto.CallbackCode;
import de.instinct.matchmaker.service.MatchmakingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matchmaking")
public class ServerMatchmakingController {
	
	private final MatchmakingService matchmakingService;
	
	@PutMapping("/callback/{lobbyUUID}")
	public ResponseEntity<?> callback(@PathVariable String lobbyUUID, @RequestBody CallbackCode code) {
		matchmakingService.callback(lobbyUUID, code);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/dispose/{lobbyUUID}")
	public ResponseEntity<?> dispose(@PathVariable String lobbyUUID) {
		matchmakingService.dispose(lobbyUUID);
		return ResponseEntity.ok().build();
	}

}
