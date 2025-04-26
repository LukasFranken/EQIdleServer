package de.instinct.game.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.engine.ai.AiEngine;
import de.instinct.engine.model.AiPlayer;
import de.instinct.engine.model.GameState;
import de.instinct.engine.model.Player;
import de.instinct.engine.net.message.types.FleetMovementMessage;
import de.instinct.engine.order.GameOrder;
import de.instinct.engine.order.types.FleetMovementOrder;
import de.instinct.game.service.model.GameSession;
import de.instinct.game.service.model.PlayerConnection;

public class SessionManager {
	
	private static List<GameSession> expiredSessions;
	private static List<GameSession> activeSessions;
	private static GameEngineInterface engineInterface;
	private static GameDataLoader gameDataLoader;
	private static AiEngine aiEngine;
	
	private static int PERIODIC_UPDATE_MS = 100;
	
	public static void init() {
		expiredSessions = new ArrayList<>();
		activeSessions = new ArrayList<>();
		engineInterface = new GameEngineInterface();
		gameDataLoader = new GameDataLoader();
		aiEngine = new AiEngine();
		
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
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
			}
        }
	}

	private static void updateClients(GameSession session) {
		for (PlayerConnection playerConnection : session.getPlayerConnections()) {
			playerConnection.connection.sendTCP(session.getGameState());
		}
	}

	public static void add(GameSession newSession) {
		GameState initialGameState = gameDataLoader.generateGameState(newSession.getGameType());
		newSession.setGameState(initialGameState);
    	newSession.setLastUpdateTimeMS(System.currentTimeMillis());
    	newSession.setActive(true);
    	
        for (PlayerConnection playerConnection : newSession.getPlayerConnections()) playerConnection.connection.sendTCP(initialGameState);
        
		activeSessions.add(newSession);
	}

	public static void process(FleetMovementMessage fleetMovement) {
		for (GameSession currentSession : activeSessions) {
    	if (currentSession.getGameState().gameUUID.contentEquals(fleetMovement.gameUUID)) {
    		FleetMovementOrder fleetMovementOrder = new FleetMovementOrder();
    		fleetMovementOrder.factionId = getFactionId(currentSession, fleetMovement.userUUID);
    		fleetMovementOrder.fromPlanetId = fleetMovement.fromPlanetId;
    		fleetMovementOrder.toPlanetId = fleetMovement.toPlanetId;
    		engineInterface.queue(currentSession.getGameState(), fleetMovementOrder);
    		updateSession(currentSession);
    		break;
    	}
    }
	}

	private static int getFactionId(GameSession currentSession, String userUUID) {
		PlayerConnection playerConnection = currentSession.getPlayerConnections().stream()
				.findFirst()
				.filter(pc -> pc.userUUID.contentEquals(userUUID))
				.orElse(null);
		return playerConnection == null ? -1 : playerConnection.factionId;
	}

	public static void create(GameSessionInitializationRequest request) {
		
	}

}
