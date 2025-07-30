package de.instinct.game.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.esotericsoftware.kryonet.Connection;

import de.instinct.api.core.API;
import de.instinct.api.game.dto.GameSessionInitializationRequest;
import de.instinct.api.game.dto.MapPreview;
import de.instinct.api.game.dto.PreviewPlanet;
import de.instinct.api.game.dto.UserTeamData;
import de.instinct.api.matchmaking.dto.CallbackCode;
import de.instinct.api.matchmaking.dto.FinishGameData;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.engine.ai.AiEngine;
import de.instinct.engine.initialization.GameStateInitialization;
import de.instinct.engine.initialization.PlanetInitialization;
import de.instinct.engine.map.GameMap;
import de.instinct.engine.model.AiPlayer;
import de.instinct.engine.model.GameState;
import de.instinct.engine.model.Player;
import de.instinct.engine.model.PlayerConnectionStatus;
import de.instinct.engine.net.message.types.FleetMovementMessage;
import de.instinct.engine.net.message.types.GamePauseMessage;
import de.instinct.engine.net.message.types.JoinMessage;
import de.instinct.engine.net.message.types.LoadedMessage;
import de.instinct.engine.net.message.types.PlayerAssigned;
import de.instinct.engine.net.message.types.SurrenderMessage;
import de.instinct.engine.order.GameOrder;
import de.instinct.engine.order.types.GamePauseOrder;
import de.instinct.engine.order.types.ShipMovementOrder;
import de.instinct.engine.order.types.SurrenderOrder;
import de.instinct.engine.util.EngineUtility;
import de.instinct.game.service.model.GameSession;
import de.instinct.game.service.model.User;

public class SessionManager {
	
	private static List<GameSession> expiredSessions;
	private static List<GameSession> activeSessions;
	private static List<GameSession> inCreationSessions;
	private static GameEngineInterface engineInterface;
	private static GameDataLoader gameDataLoader;
	private static AiEngine aiEngine;
	
	private static int PERIODIC_UPDATE_MS = 50;
	private static int PERIODIC_CLIENT_UPDATE_MS = 500;
	private static ScheduledExecutorService scheduler;
	
	public static void init() {
		expiredSessions = new ArrayList<>();
		activeSessions = new ArrayList<>();
		inCreationSessions = new ArrayList<>();
		engineInterface = new GameEngineInterface();
		gameDataLoader = new GameDataLoader();
		aiEngine = new AiEngine();
		
		scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
		    @Override
		    public Thread newThread(Runnable r) {
		        Thread thread = new Thread(r);
		        thread.setUncaughtExceptionHandler((t, e) -> {
		            System.err.println("Unhandled exception in thread " + t.getName() + ": " + e);
		            e.printStackTrace();
		        });
		        return thread;
		    }
		});
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
		if (session.getGameType().getVersusMode() == VersusMode.AI) {
			for (Player player : session.getGameState().players) {
				if (player instanceof AiPlayer) {
					List<GameOrder> aiOrders = aiEngine.act((AiPlayer)player, session.getGameState());
					engineInterface.queueAll(session.getGameState(), aiOrders);
				}
			}
		}
	}

	private static void updateSession(GameSession session) {
		engineInterface.updateGameState(session);
		boolean clientUpdateRequired = engineInterface.containedValidOrders();
		if (session.getGameState().winner != 0) {
			clientUpdateRequired = true;
			expiredSessions.add(session);
			activeSessions.remove(session);
			
			FinishGameData finishData = new FinishGameData();
			finishData.setPlayedMS(session.getGameState().gameTimeMS);
			finishData.setWinnerTeamId(session.getGameState().winner);
			API.matchmaking().finish(session.getUuid(), finishData);
		}
		if (System.currentTimeMillis() - session.getLastClientUpdateTimeMS() >= PERIODIC_CLIENT_UPDATE_MS) {
			clientUpdateRequired = true;
		}
        if (clientUpdateRequired) {
        	updateClients(session);
        }
	}

	private static void updateClients(GameSession session) {
		for (User user : session.getUsers()) {
			if (user.getConnection() != null && user.getConnection().isConnected()) {
				user.getConnection().sendTCP(session.getGameState());
			}
		}
		session.setLastClientUpdateTimeMS(System.currentTimeMillis());
	}

	public static void process(FleetMovementMessage fleetMovement) {
		for (GameSession currentSession : activeSessions) {
			if (currentSession.getGameState().gameUUID.contentEquals(fleetMovement.gameUUID)) {
				ShipMovementOrder shipMovementOrder = new ShipMovementOrder();
				shipMovementOrder.playerId = getPlayerId(currentSession, fleetMovement.userUUID);
				shipMovementOrder.fromPlanetId = fleetMovement.fromPlanetId;
				shipMovementOrder.toPlanetId = fleetMovement.toPlanetId;
				shipMovementOrder.playerShipId = fleetMovement.shipId;
				engineInterface.queue(currentSession.getGameState(), shipMovementOrder);
				updateSession(currentSession);
				break;
			}
		}
	}
	
	public static void process(GamePauseMessage gamePause) {
		for (GameSession currentSession : activeSessions) {
			if (currentSession.getGameState().gameUUID.contentEquals(gamePause.gameUUID)) {
				GamePauseOrder order = new GamePauseOrder();
				order.playerId = getPlayerId(currentSession, gamePause.userUUID);
				order.pause = gamePause.pause;
				order.reason = gamePause.reason;
				engineInterface.queue(currentSession.getGameState(), order);
				updateSession(currentSession);
			}
		}
	}
	
	public static void process(SurrenderMessage surrender) {
		for (GameSession currentSession : activeSessions) {
			if (currentSession.getGameState().gameUUID.contentEquals(surrender.gameUUID)) {
				SurrenderOrder order = new SurrenderOrder();
				order.playerId = getPlayerId(currentSession, surrender.userUUID);
				engineInterface.queue(currentSession.getGameState(), order);
				updateSession(currentSession);
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
				try {
					startSession(uuid, request);
				} catch (Exception e) {
					System.err.println("Error starting session: " + e.getMessage());
					e.printStackTrace();
				}
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
		
		GameStateInitialization initialization = gameDataLoader.generateGameStateInitialization(session);
		session.setGameState(engineInterface.initializeGameState(initialization));
		readyUpAI(session.getGameState());
		inCreationSessions.add(session);
		System.out.println("Starting session: " + initialization.gameUUID);
		API.matchmaking().callback(session.getUuid(), CallbackCode.READY);
	}

	private static void readyUpAI(GameState state) {
		for (Player player : state.players) {
			if (player instanceof AiPlayer) {
				AiPlayer aiPlayer = (AiPlayer) player;
				for (PlayerConnectionStatus status : state.connectionStati) {
					if (status.playerId == aiPlayer.id) {
						status.connected = true;
						status.loaded = true;
					}
				}
			}
		}
	}

	private static List<User> loadUsers(GameSessionInitializationRequest request) {
		List<User> users = new ArrayList<>();
		for (UserTeamData userData : request.getUsers()) {
			System.out.println(userData);
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
					EngineUtility.getPlayerConnectionStatus(session.getGameState().connectionStati, user.getPlayerId()).connected = true;
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

	public static void loaded(LoadedMessage loadedMessage, Connection c) {
		for (GameSession session : activeSessions) {
			for (User user : session.getUsers()) {
				if (user.getUuid().contentEquals(loadedMessage.playerUUID)) {
					EngineUtility.getPlayerConnectionStatus(session.getGameState().connectionStati, user.getPlayerId()).loaded = true;
					System.out.println("game loaded: id " + user.getPlayerId() + " - " + user.getName());
					checkGameStart(session);
					return;
				}
			}
		}
	}

	private static void checkGameStart(GameSession session) {
		boolean canStart = true;
		for (User user : session.getUsers()) {
			Player player = EngineUtility.getPlayer(session.getGameState().players, user.getPlayerId());
			PlayerConnectionStatus status = EngineUtility.getPlayerConnectionStatus(session.getGameState().connectionStati, user.getPlayerId());
			if (!status.loaded && player.teamId != 0) canStart = false;
		}
		if (canStart) {
			session.getGameState().started = true;
		}
		updateClients(session);
	}

	public static void disconnect(Connection c) {
		for (GameSession session : inCreationSessions) {
			for (User user : session.getUsers()) {
				if (user.getConnection() == c) {
					EngineUtility.getPlayerConnectionStatus(session.getGameState().connectionStati, user.getPlayerId()).connected = false;
					System.out.println("disconnected: id " + user.getPlayerId() + " - " + user.getName());
					updateClients(session);
					return;
				}
			}
		}
	}

	public static MapPreview preview(String map) {
		GameMap gameMap = gameDataLoader.preview(map);
		MapPreview mapPreview = new MapPreview();
		mapPreview.setPlanets(new ArrayList<>());
		if (gameMap == null) return mapPreview;
		
		List<PreviewPlanet> planets = new ArrayList<>();
		for (PlanetInitialization planetInit : gameMap.planets) {
			PreviewPlanet planet = new PreviewPlanet();
			planet.setXPos(planetInit.position.x);
			planet.setYPos(planetInit.position.y);
			planet.setAncient(planetInit.ancient);
			planet.setOwnerId(planetInit.ownerId);
			planets.add(planet);
		}
		mapPreview.setPlanets(planets);
		return mapPreview;
	}

}
