package de.instinct.matchmaking.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.core.API;
import de.instinct.api.core.model.GeneralRequestResponse;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.InviteResponse;
import de.instinct.api.matchmaking.dto.InvitesStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyCreationResponse;
import de.instinct.api.matchmaking.dto.LobbyStatusResponse;
import de.instinct.api.matchmaking.dto.LobbyTypeSetResponse;
import de.instinct.api.matchmaking.dto.MatchmakingRegistrationResponseCode;
import de.instinct.api.matchmaking.dto.MatchmakingStatusResponse;
import de.instinct.api.matchmaking.model.GameType;
import de.instinct.matchmaking.service.MatchmakingService;
import de.instinct.matchmaking.service.impl.MatchmakingServiceImpl;

@RestController
@RequestMapping("/matchmaking")
public class MatchmakingController extends BaseServiceController {
	
	private final MatchmakingService matchmakingService;
	
	public MatchmakingController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("matchmaking", serverPort, version);
		matchmakingService = new MatchmakingServiceImpl();
	}
	
	@Override
	protected void connectToAPIs() {
		API.game().connect();
		API.meta().connect();
	}
	
	@PostMapping("/create")
	public ResponseEntity<LobbyCreationResponse> create(@RequestHeader("token") String authToken) {
		return ResponseEntity.ok(matchmakingService.createLobby(authToken));
	}
	
	@GetMapping("/get")
	public ResponseEntity<String> get(@RequestHeader("token") String authToken) {
		return ResponseEntity.ok(matchmakingService.getUserLobby(authToken));
	}
	
	@PostMapping("/settype/{lobbyUUID}")
	public ResponseEntity<LobbyTypeSetResponse> settype(@RequestHeader("token") String authToken, @PathVariable String lobbyUUID, @RequestBody GameType selectedGameType) {
		return ResponseEntity.ok(matchmakingService.setType(authToken, lobbyUUID, selectedGameType));
	}
	
	@PostMapping("/invite/{username}")
	public ResponseEntity<InviteResponse> invite(@RequestHeader("token") String authToken, @PathVariable String username) {
		return ResponseEntity.ok(matchmakingService.invite(authToken, username));
	}
	
	@PostMapping("/accept/{lobbyUUID}")
	public ResponseEntity<String> accept(@RequestHeader("token") String authToken, @PathVariable String lobbyUUID) {
		return ResponseEntity.ok(matchmakingService.respond(authToken, lobbyUUID, true));
	}
	
	@PostMapping("/decline/{lobbyUUID}")
	public ResponseEntity<?> decline(@RequestHeader("token") String authToken, @PathVariable String lobbyUUID) {
		matchmakingService.respond(authToken, lobbyUUID, false);
		return ResponseEntity.ok("OK");
	}
	
	@GetMapping("/invites")
	public ResponseEntity<InvitesStatusResponse> invites(@RequestHeader("token") String authToken) {
		return ResponseEntity.ok(matchmakingService.getInvites(authToken));
	}
	
	@PostMapping("/start/{lobbyUUID}")
	public ResponseEntity<MatchmakingRegistrationResponseCode> start(@RequestHeader("token") String authToken, @PathVariable String lobbyUUID) {
		return ResponseEntity.ok(matchmakingService.start(authToken, lobbyUUID));
	}
	
	@GetMapping("/status/{lobbyUUID}")
	public ResponseEntity<LobbyStatusResponse> status(@PathVariable("lobbyUUID") String lobbyUUID) {
		return ResponseEntity.ok(matchmakingService.getStatus(lobbyUUID));
	}
	
	@GetMapping("/matchmaking/{lobbyUUID}")
	public ResponseEntity<MatchmakingStatusResponse> matchmaking(@PathVariable("lobbyUUID") String lobbyUUID) {
		return ResponseEntity.ok(matchmakingService.getMatchmakingStatus(lobbyUUID));
	}
	
	@PutMapping("/callback/{gamesessionUUID}")
	public ResponseEntity<GeneralRequestResponse> callback(@PathVariable String gamesessionUUID, @RequestBody CallbackCode code) {
		matchmakingService.callback(gamesessionUUID, code);
		return ResponseEntity.ok(GeneralRequestResponse.OK);
	}
	
	@PutMapping("/finish/{gamesessionUUID}")
	public ResponseEntity<GeneralRequestResponse> finish(@PathVariable String gamesessionUUID) {
		matchmakingService.finish(gamesessionUUID);
		return ResponseEntity.ok(GeneralRequestResponse.OK);
	}
	
	@PutMapping("/dispose/{gamesessionUUID}")
	public ResponseEntity<GeneralRequestResponse> dispose(@PathVariable String gamesessionUUID) {
		matchmakingService.dispose(gamesessionUUID);
		return ResponseEntity.ok(GeneralRequestResponse.OK);
	}
	
}
