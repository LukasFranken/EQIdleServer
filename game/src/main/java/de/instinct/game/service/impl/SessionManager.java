package de.instinct.game.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.esotericsoftware.kryonet.Connection;

import de.instinct.api.core.API;
import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.game.dto.UserData;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.engine.ai.AiEngine;
import de.instinct.engine.model.AiPlayer;
import de.instinct.engine.model.Player;
import de.instinct.engine.net.message.types.FleetMovementMessage;
import de.instinct.engine.net.message.types.JoinMessage;
import de.instinct.engine.net.message.types.PlayerAssigned;
import de.instinct.engine.order.GameOrder;
import de.instinct.engine.order.types.FleetMovementOrder;
import de.instinct.game.service.model.GameSession;
import de.instinct.game.service.model.User;

public class SessionManager {
	
	private static List<GameSession> expiredSessions;
	private static List<GameSession> activeSessions;
	private static List<GameSession> inCreationSessions;
	private static GameEngineInterface engineInterface;
	private static GameDataLoader gameDataLoader;
	private static AiEngine aiEngine;
	
	private static int PERIODIC_UPDATE_MS = 100;
	private static ScheduledExecutorService scheduler;
	
	public static void init() {
		expiredSessions = new ArrayList<>();
		activeSessions = new ArrayList<>();
		inCreationSessions = new ArrayList<>();
		engineInterface = new GameEngineInterface();
		gameDataLoader = new GameDataLoader();
		aiEngine = new AiEngine();
		
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> {
			List<GameSession> sessionsSnapshot = new ArrayList<>(activeSessions);
	        for (GameSession session : sessionsSnapshot) {
	            try {
	                updateAi(session);
	                updateSession(session);
	            } catch (Exception e) {
	                System.err.println("Error in session update: " + e.getMessage());
	                e.printStackTrace();
	            }
	        }
		}, 0, PERIODIC_UPDATE_MS, TimeUnit.MILLISECONDS);
	}
	
	private static void updateAi(GameSession session) {
		if (session.getGameType().versusMode == VersusMode.AI) {
			for (Player player : session.getGameState().players) {
				if (player instanceof AiPlayer) {
					List<GameOrder> aiOrders = aiEngine.act((AiPlayer)player, session.getGameState());
					engineInterface.queueAll(session.getGameState(), aiOrders);
				}
			}
		}
	}

	private static void updateSession(GameSession session) {
		boolean clientUpdateRequired = engineInterface.updateGameState(session);
        if (clientUpdateRequired) {
        	updateClients(session);
        	if (session.getGameState().winner != 0) {
				expiredSessions.add(session);
				activeSessions.remove(session);
				API.matchmaking().finish(session.getUuid());
			}
        }
	}

	private static void updateClients(GameSession session) {
		for (User user : session.getUsers()) {
			if (user.getConnection() != null && user.getConnection().isConnected()) {
				user.getConnection().sendTCP(session.getGameState());
			}
		}
	}

	public static void process(FleetMovementMessage fleetMovement) {
		for (GameSession currentSession : activeSessions) {
    	if (currentSession.getGameState().gameUUID.contentEquals(fleetMovement.gameUUID)) {
    		FleetMovementOrder fleetMovementOrder = new FleetMovementOrder();
    		fleetMovementOrder.playerId = getPlayerId(currentSession, fleetMovement.userUUID);
    		fleetMovementOrder.fromPlanetId = fleetMovement.fromPlanetId;
    		fleetMovementOrder.toPlanetId = fleetMovement.toPlanetId;
    		engineInterface.queue(currentSession.getGameState(), fleetMovementOrder);
    		updateSession(currentSession);
    		break;
    	}
    }
	}

	private static int getPlayerId(GameSession currentSession, String userUUID) {
		User currentUser = currentSession.getUsers().stream()
				.filter(user -> user.getUuid().contentEquals(userUUID))
				.findFirst()
				.orElse(null);
		return currentUser == null ? -1 : currentUser.getPlayerId();
	}

	public static String create(GameSessionInitializationRequest request) {
		String uuid = UUID.randomUUID().toString();
		
		scheduler.execute(new Runnable() {
			
			@Override
			public void run() {
				startSession(uuid, request);
			}
			
		});
		
		return uuid;
	}

	private static void startSession(String uuid, GameSessionInitializationRequest request) {
		GameSession session = GameSession.builder()
				.uuid(uuid)
				.gameType(request.getType())
				.users(loadUsers(request))
				.build();
		
		session.setGameState(gameDataLoader.generateGameState(session));
		inCreationSessions.add(session);
		API.matchmaking().callback(session.getUuid(), CallbackCode.READY);
	}

	private static List<User> loadUsers(GameSessionInitializationRequest request) {
		List<User> users = new ArrayList<>();
		for (UserData userData : request.getUsers()) {
			users.add(User.builder()
					.name(API.meta().profile(userData.getUuid()).getUsername())
					.uuid(userData.getUuid())
					.teamid(userData.getTeamId())
					.loadout(API.meta().loadout(userData.getUuid()))
					.build());
		}
		return users;
	}

	public static void join(JoinMessage joinMessage, Connection connection) {
		for (GameSession session : inCreationSessions) {
			for (User user : session.getUsers()) {
				if (user.getUuid().contentEquals(joinMessage.playerUUID)) {
					user.setConnection(connection);
					PlayerAssigned playerAssigned = new PlayerAssigned();
					playerAssigned.playerId = user.getPlayerId();
					user.getConnection().sendTCP(playerAssigned);
					System.out.println("assigning id " + playerAssigned.playerId + " to " + user.getName());
					checkKickoff(session);
					return;
				}
			}
		}
		for (GameSession session : activeSessions) {
			for (User user : session.getUsers()) {
				if (user.getUuid().contentEquals(joinMessage.playerUUID)) {
					user.setConnection(connection);
					PlayerAssigned playerAssigned = new PlayerAssigned();
					playerAssigned.playerId = user.getPlayerId();
					user.getConnection().sendTCP(playerAssigned);
					user.getConnection().sendTCP(session.getGameState());
				}
			}
		}
	}

	private static void checkKickoff(GameSession session) {
		for (User checkUser : session.getUsers()) {
			if (checkUser.getConnection() == null) {
				return;
			}
		}
		
		inCreationSessions.remove(session);
		session.setLastUpdateTimeMS(System.currentTimeMillis());
		activeSessions.add(session);
		updateClients(session);
	}

}
