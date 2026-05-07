package de.instinct.mining.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;

import de.instinct.api.core.API;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.mining.dto.CreateSessionRequest;
import de.instinct.api.mining.dto.CreateSessionResponse;
import de.instinct.api.mining.dto.Maps;
import de.instinct.api.mining.dto.player.MiningPlayerData;
import de.instinct.api.mining.dto.player.MiningPlayerFeatureData;
import de.instinct.api.mining.dto.player.MiningPlayerMissionData;
import de.instinct.api.mining.dto.player.MissionData;
import de.instinct.engine.core.net.GameOrderMessage;
import de.instinct.engine.core.order.GameOrder;
import de.instinct.engine.mining.entity.ship.MiningPlayerShip;
import de.instinct.engine.mining.entity.ship.cargo.CargoItem;
import de.instinct.engine.mining.net.message.ConnectMessage;
import de.instinct.engine.mining.net.message.OnboardMessage;
import de.instinct.engine.mining.net.message.StartMessage;
import de.instinct.engine_api.mining.MiningStateManager;
import de.instinct.engine_api.mining.model.MiningPlayerInventoryData;
import de.instinct.engine_api.mining.service.model.MiningMissionOverview;
import de.instinct.mining.service.model.MiningClient;
import de.instinct.mining.service.model.Session;

public class SessionController {
	
	private Map<String, MiningPlayerData> playerDatas;
	private Map<String, MiningPlayerInventoryData> playerInventories;
	private List<Session> sessions;
	private MiningStateManager stateManager;
	private MiningDataLoader dataLoader;
	
	public SessionController() {
		this.sessions = new ArrayList<>();
		stateManager = new MiningStateManager();
		playerDatas = new HashMap<>();
		playerInventories = new HashMap<>();
		dataLoader = new MiningDataLoader();
	}
	
	private MiningPlayerData getPlayerData(String playerUUID) {
		MiningPlayerData data = playerDatas.get(playerUUID);
		if (data == null) {
			data = initializePlayerData(playerUUID);
		}
		return data;
	}

	private MiningPlayerData initializePlayerData(String playerUUID) {
		MiningPlayerData data = MiningPlayerData.builder()
				.uuid(playerUUID)
				.shipData(dataLoader.loadBaseShipData())
				.featureData(MiningPlayerFeatureData.builder()
						.build())
				.missionData(MiningPlayerMissionData.builder()
						.missions(new ArrayList<>())
						.build())
				.build();
		playerDatas.put(playerUUID, data);
		MiningPlayerInventoryData inventoryData = new MiningPlayerInventoryData();
		inventoryData.setResources(new HashMap<>());
		playerInventories.put(playerUUID, inventoryData);
		return data;
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
					stateManager.integrateOrder(session.getState(), message.order);
					stateManager.updateGameState(session.getState(), System.currentTimeMillis() - session.getLastUpdateTimeMS());
					session.setLastUpdateTimeMS(System.currentTimeMillis());
					updateClients(session);
				}
			}
		}
		sessions.removeIf(Session::isFinished);
	}

	private void updateClients(Session session) {
		GameOrder lastOrder = stateManager.getLastOrder(session.getState());
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
		if (stateManager.checkFinished(session.getState())) {
			System.out.println("Ending session: " + session.getState().metaData.gameUUID);
			end(session);
		}
	}

	private void end(Session session) {
		MiningMissionOverview overview = dataLoader.loadMissionOverview(session.getMapName());
		for (MiningClient client : session.getClients()) {
			MiningPlayerInventoryData inventoryData = playerInventories.get(client.getUuid());
			for (MiningPlayerShip ship : session.getState().entityData.playerShips) {
				if (ship.recalled) {
					for (CargoItem resource : ship.cargo.items) {
						if (inventoryData.getResources().containsKey(resource.resourceType)) {
							float currentAmount = inventoryData.getResources().get(resource.resourceType);
							inventoryData.getResources().put(resource.resourceType, currentAmount + resource.amount);
						} else {
							inventoryData.getResources().put(resource.resourceType, resource.amount);
						}
					}
					MissionData currentMissionData = null;
					for (MissionData missionData : client.getPlayerData().getMissionData().getMissions()) {
						if (missionData.getName().contentEquals(session.getMapName())) {
							currentMissionData = missionData;
						}
					}
					int minedAsteroids = session.getState().minedAsteroids;
					if (currentMissionData == null) {
						currentMissionData = new MissionData();
						currentMissionData.setName(session.getMapName());
						currentMissionData.setMinedAsteroids(minedAsteroids);
						client.getPlayerData().getMissionData().getMissions().add(currentMissionData);
					} else {
						if (currentMissionData.getMinedAsteroids() < minedAsteroids) {
							currentMissionData.setMinedAsteroids(minedAsteroids);
						}
					}
					currentMissionData.setCompleted(currentMissionData.getMinedAsteroids() >= overview.getAsteroids());
				}
			}
		}
		session.setFinished(true);
	}

	public void disconnect(Connection c) {
		//identify client
		//remove connection from client
		//pause session
		//notify other clients
		//if all clients disconnected, end session?
	}

	public CreateSessionResponse createSession(CreateSessionRequest request) {
		for (Session session : sessions) {
			for (String playerUUID : request.getPlayerUUIDs()) {
				for (MiningClient client : session.getClients()) {
					if (client.getUuid().contentEquals(playerUUID)) {
						return CreateSessionResponse.ALREADY_IN_SESSION;
					}
				}
			}
		}
		
		Session session = new Session();
		session.setMapName(request.getMap());
		session.setClients(new ArrayList<>());
		int playerId = 1;
		for (String playerUUID : request.getPlayerUUIDs()) {
			MiningClient client = new MiningClient();
			client.setPlayerId(playerId);
			client.setUuid(playerUUID);
			ProfileData profileData = API.meta().profile(playerUUID);
			if (profileData != null) client.setName(profileData.getUsername());
			client.setPlayerData(getPlayerData(playerUUID));
			session.getClients().add(client);
			playerId++;
		}
		session.setState(dataLoader.loadState(request.getMap(), session.getClients()));
		sessions.add(session);
		return CreateSessionResponse.SUCCESS;
	}

	public MiningPlayerInventoryData getPlayerInventory(String token) {
		return playerInventories.get(token);
	}

	public MiningPlayerMissionData getPlayerMissionData(String token) {
		MiningPlayerData playerData = getPlayerData(token);
		return playerData.getMissionData();
	}

	public MiningMissionOverview getMissionOverview(String missionName) {
		return dataLoader.loadMissionOverview(missionName);
	}

	public Maps getMaps() {
		return dataLoader.loadMaps();
	}

}
