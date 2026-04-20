package de.instinct.mining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import de.instinct.api.core.API;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;
import de.instinct.engine.core.net.GameOrderMessage;
import de.instinct.engine.core.order.GameOrder;
import de.instinct.engine.mining.data.MiningGameState;
import de.instinct.engine.mining.net.message.ConnectMessage;
import de.instinct.engine.mining.net.message.OnboardMessage;
import de.instinct.engine.mining.net.message.StartMessage;
import de.instinct.engine.mining.player.MiningPlayer;
import de.instinct.engine_api.mining.MiningEngineInterface;
import de.instinct.engine_api.mining.model.MiningGameStateInitialization;
import de.instinct.mining.service.model.MiningClient;
import de.instinct.mining.service.model.Session;

public class SessionController {
	
	private List<Session> sessions;
	private MiningEngineInterface engineInterface;
	
	public SessionController() {
		this.sessions = new ArrayList<>();
		engineInterface = new MiningEngineInterface();
	}

	public void processMessage(Connection c, Object o) {
		if (o instanceof ConnectMessage) {
			ConnectMessage message = (ConnectMessage) o;
			for (Session session : sessions) {
				for (MiningClient client : session.getClients()) {
					if (client.getUuid().contentEquals(message.senderUUID)) {
						OnboardMessage onboardMessage = new OnboardMessage();
						onboardMessage.senderUUID = "server";
						onboardMessage.initialGameState = session.getState();
						onboardMessage.assignedPlayerId = client.getPlayerId();
						c.sendTCP(onboardMessage);
						client.setConnection(c);
						break;
					}
				}
				for (MiningClient client : session.getClients()) {
					if (client.getConnection() == null) {
						return;
					}
				}
				session.getState().metaData.started = true;
				session.setLastUpdateTimeMS(System.currentTimeMillis());
				for (MiningClient client : session.getClients()) {
					StartMessage startMessage = new StartMessage();
					startMessage.senderUUID = "server";
					client.getConnection().sendTCP(startMessage);
				}
			}
		}
		if (o instanceof GameOrderMessage) {
			GameOrderMessage message = (GameOrderMessage) o;
			for (Session session : sessions) {
				if (session.getState().metaData.gameUUID.contentEquals(message.gameUUID)) {
					engineInterface.integrateOrder(session.getState(), message.order);
					engineInterface.updateGameState(session.getState(), System.currentTimeMillis() - session.getLastUpdateTimeMS());
					session.setLastUpdateTimeMS(System.currentTimeMillis());
					updateClients(session);
				}
			}
		}
	}

	private void updateClients(Session session) {
		GameOrder lastOrder = engineInterface.getLastOrder(session.getState());
		if (lastOrder != null) {
			if (lastOrder.processGameTimeStamp > session.getLastProcessedOrderTimeStamp()) {
				session.setLastProcessedOrderTimeStamp(lastOrder.processGameTimeStamp);
				for (MiningClient client : session.getClients()) {
					GameOrderMessage gameOrderMessage = new GameOrderMessage();
					gameOrderMessage.gameUUID = session.getState().metaData.gameUUID;
					gameOrderMessage.senderUUID = "server";
					gameOrderMessage.order = lastOrder;
					client.getConnection().sendTCP(gameOrderMessage);
				}
			}
		}
		if (engineInterface.checkRecalled(session.getState())) {
			System.out.println("Successfully recalled, ending session.");
			return;
		}
		if (engineInterface.checkFailed(session.getState())) {
			System.out.println("Extraction failed, ending session.");
			return;
		}
	}

	public void disconnect(Connection c) {
		//identify client
		//remove connection from client
		//pause session
		//notify other clients
		//if all clients disconnected, end session?
	}

	public CreateSessionResponse createSession(CreateSessionRequest request) {
		Session session = new Session();
		session.setClients(new ArrayList<>());
		int playerId = 1;
		for (String playerUUID : request.getPlayerUUIDs()) {
			MiningClient client = new MiningClient();
			client.setPlayerId(playerId);
			client.setUuid(playerUUID);
			ProfileData profileData = API.meta().profile(playerUUID);
			if (profileData != null) client.setName(profileData.getUsername());
			session.getClients().add(client);
			playerId++;
		}
		
		MiningGameStateInitialization initialization = new MiningGameStateInitialization();
		initialization.setGameUUID(UUID.randomUUID().toString());
		initialization.setPlayers(new ArrayList<>());
		for (MiningClient client : session.getClients()) {
			MiningPlayer player = engineInterface.getTestPlayer();
			player.id = client.getPlayerId();
			initialization.getPlayers().add(player);
		}
		initialization.setMap(null); //impl
		initialization.setPauseCountLimit(3);
		initialization.setPauseTimeLimitMS(60_000);
		MiningGameState state = engineInterface.initializeMining(initialization);
		session.setState(state);
		sessions.add(session);
		return CreateSessionResponse.SUCCESS;
	}

}
