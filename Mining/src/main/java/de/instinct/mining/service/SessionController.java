package de.instinct.mining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import de.instinct.api.core.API;
import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;
import de.instinct.engine.mining.data.MiningGameState;
import de.instinct.engine.mining.net.message.ConnectMessage;
import de.instinct.engine.mining.player.MiningPlayer;
import de.instinct.engine_api.mining.MiningGameStateInitializer;
import de.instinct.engine_api.mining.model.MiningGameStateInitialization;
import de.instinct.mining.service.model.MiningClient;
import de.instinct.mining.service.model.Session;

public class SessionController {
	
	private List<Session> sessions;
	private MiningGameStateInitializer stateInitializer;
	
	public SessionController() {
		this.sessions = new ArrayList<>();
		stateInitializer = new MiningGameStateInitializer();
	}

	public void processMessage(Connection c, Object o) {
		if (o instanceof ConnectMessage) {
			ConnectMessage message = (ConnectMessage) o;
			
		}
	}

	public void disconnect(Connection c) {
		
	}

	public CreateSessionResponse createSession(CreateSessionRequest request) {
		Session session = new Session();
		session.setClients(new ArrayList<>());
		int playerId = 1;
		for (String playerUUID : request.getPlayerUUIDs()) {
			MiningClient client = new MiningClient();
			client.setPlayerId(playerId);
			client.setUuid(playerUUID);
			client.setName(API.meta().profile(playerUUID).getUsername());
			session.getClients().add(client);
			playerId++;
		}
		
		MiningGameStateInitialization initialization = new MiningGameStateInitialization();
		initialization.setGameUUID(UUID.randomUUID().toString());
		initialization.setPlayers(new ArrayList<>());
		for (MiningClient client : session.getClients()) {
			MiningPlayer player = stateInitializer.getTestPlayer();
			player.id = client.getPlayerId();
			initialization.getPlayers().add(player);
		}
		initialization.setMap(null); //impl
		initialization.setPauseCountLimit(3);
		initialization.setPauseTimeLimitMS(60_000);
		MiningGameState state = stateInitializer.initializeMining(initialization);
		session.setState(state);
		sessions.add(session);
		return CreateSessionResponse.SUCCESS;
	}

}
