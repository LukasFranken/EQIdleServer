package de.instinct.commander.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.instinct.api.commander.dto.CommanderData;
import de.instinct.api.commander.dto.CommanderInitializationResponseCode;
import de.instinct.api.commander.dto.CommanderRankUpResponseCode;
import de.instinct.api.commander.dto.RankUpCommanderUpgrade;
import de.instinct.api.core.API;
import de.instinct.api.meta.dto.PlayerRank;
import de.instinct.base.controller.BaseServiceController;
import de.instinct.commander.service.CommanderService;
import de.instinct.commander.service.impl.CommanderServiceImpl;

@RestController
@RequestMapping("/commander")
public class CommanderController extends BaseServiceController {
	
	private final CommanderService service;

	public CommanderController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("commander", serverPort, version);
		service = new CommanderServiceImpl();
	}
	
	@Override
	protected void connectToAPIs() {
		API.meta().connect();
	}
	
	@GetMapping("/init/{token}")
	public ResponseEntity<CommanderInitializationResponseCode> init(@PathVariable String token) {
		return ResponseEntity.ok(service.initialize(token));
	}
	
	@GetMapping("/data/{token}")
	public ResponseEntity<CommanderData> data(@PathVariable String token) {
		return ResponseEntity.ok(service.data(token));
	}
	
	@PostMapping("/rankup/{token}")
	public ResponseEntity<CommanderRankUpResponseCode> rankup(@PathVariable String token, @RequestBody PlayerRank rank) {
		return ResponseEntity.ok(service.rankup(token, rank));
	}
	
	@PostMapping("/upgrade")
	public ResponseEntity<RankUpCommanderUpgrade> upgrade(@RequestBody PlayerRank rank) {
		return ResponseEntity.ok(service.upgrade(rank));
	}

}
