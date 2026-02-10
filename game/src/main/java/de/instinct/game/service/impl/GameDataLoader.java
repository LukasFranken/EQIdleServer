package de.instinct.game.service.impl;

import java.util.ArrayList;
import java.util.List;

import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.base.file.FileManager;
import de.instinct.engine.model.AiPlayer;
import de.instinct.engine.model.Player;
import de.instinct.engine_api.ai.service.AIPlayerLoader;
import de.instinct.engine_api.ai.service.NeutralPlayerLoader;
import de.instinct.engine_api.core.EngineAPI;
import de.instinct.engine_api.core.model.GameMap;
import de.instinct.engine_api.core.model.GameStateInitialization;
import de.instinct.engine_api.core.service.EngineDataInterface;
import de.instinct.game.service.model.GameSession;
import de.instinct.game.service.model.User;

public class GameDataLoader {
	
	private static final String MAP_FILE_SUBFOLDER = "maps";
	private static final String MAP_FILE_POSTFIX = ".map";
	
	private AIPlayerLoader aiPlayerLoader = new AIPlayerLoader();
	private NeutralPlayerLoader neutralPlayerLoader = new NeutralPlayerLoader();
	
	public GameDataLoader() {
		aiPlayerLoader = new AIPlayerLoader();
		neutralPlayerLoader = new NeutralPlayerLoader();
	}
	
	public GameStateInitialization generateGameStateInitialization(GameSession session) {
		GameStateInitialization initialGameState = loadInitialMap(session);
		initialGameState.setGameUUID(session.getUuid());
		initialGameState.setPlayers(loadPlayers(session));
		initialGameState.setAncientPlanetResourceDegradationFactor(0.5f);;
		initialGameState.setGameTimeLimitMS((int)session.getGameType().getDuration());
		initialGameState.setAtpToWin(session.getGameType().getApRequired());
		initialGameState.setPauseTimeLimitMS(20_000);
		initialGameState.setPauseCountLimit(3);
		return initialGameState;
	}

	private GameStateInitialization loadInitialMap(GameSession session) {
		GameStateInitialization initialGameState = new GameStateInitialization();
		initialGameState.setMap(ObjectJSONMapper.mapJSON(FileManager.loadFile(
				MAP_FILE_SUBFOLDER 
				+ "/" + session.getGameType().getGameMode().toString().toLowerCase() 
				+ "/" + session.getGameType().getFactionMode().toString().toLowerCase() 
				+ "/" + session.getGameType().getMap() + MAP_FILE_POSTFIX), GameMap.class));
		System.out.println("Map: " + initialGameState.getMap() + " for game mode: " + session.getGameType());
		return initialGameState;
	}
	
	public GameMap preview(String map) {
		String mapFile = FileManager.loadFile(MAP_FILE_SUBFOLDER + "/conquest/one_vs_one/" + map + MAP_FILE_POSTFIX);
		if (mapFile == null || mapFile.isEmpty()) return null;
		return ObjectJSONMapper.mapJSON(mapFile, GameMap.class);
	}

	public List<Player> loadPlayers(GameSession session) {
		List<Player> players = new ArrayList<>();
		
		Player neutralPlayer = neutralPlayerLoader.createNeutralPlayer(session.getGameType().getThreatLevel());
		neutralPlayer.id = 0;
		neutralPlayer.teamId = 0;
		neutralPlayer.name = "Neutral Player";
		neutralPlayer.ships = new ArrayList<>();
		players.add(neutralPlayer);
		
		int id = 1;
		for (User user : session.getUsers()) {
			if (user.getTeamid() == 1) {
				Player userPlayer = getPlayer(user);
				userPlayer.id = id;
				user.setPlayerId(userPlayer.id);
				players.add(userPlayer);
				id++;
			}
		}
		
		if (session.getGameType().getVersusMode() == VersusMode.AI) {
			for (int i = 0; i < session.getGameType().getFactionMode().teamPlayerCount; i++) {
				AiPlayer aiPlayer = aiPlayerLoader.initialize(session.getGameType().getThreatLevel());
				aiPlayer.id = id;
				aiPlayer.teamId = 2;
				players.add(aiPlayer);
				id++;
			}
		} else {
			for (User user : session.getUsers()) {
				if (user.getTeamid() == 2) {
					Player userPlayer = getPlayer(user);
					userPlayer.id = id;
					user.setPlayerId(userPlayer.id);
					players.add(userPlayer);
					id++;
				}
			}
		}
		
		return players;
	}

	public Player getPlayer(User user) {
		Player newPlayer = new Player();
		newPlayer.teamId = user.getTeamid();
		newPlayer.name = user.getName();
		newPlayer.commandPointsGenerationSpeed = user.getLoadout().getCommander().getCommandPointsGenerationSpeed();
		newPlayer.startCommandPoints = user.getLoadout().getCommander().getStartCommandPoints();
		newPlayer.maxCommandPoints = user.getLoadout().getCommander().getMaxCommandPoints();
		newPlayer.planetData = EngineDataInterface.getPlanetData(user.getLoadout().getPlayerInfrastructure(), EngineAPI.construction().construction());
		newPlayer.ships = EngineDataInterface.getShips(user.getLoadout().getShips(), EngineAPI.shipyard().shipyard());
		newPlayer.turrets = EngineDataInterface.getPlayerTurretData(user.getLoadout().getPlayerInfrastructure(), EngineAPI.construction().construction());
		return newPlayer;
	}
	
}
