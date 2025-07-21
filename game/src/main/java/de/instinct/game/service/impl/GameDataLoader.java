package de.instinct.game.service.impl;

import java.util.ArrayList;
import java.util.List;

import base.file.FileManager;
import de.instinct.api.core.API;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.game.engine.EngineInterface;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.engine.ai.AiEngine;
import de.instinct.engine.ai.difficulty.AiDifficulty;
import de.instinct.engine.initialization.GameStateInitialization;
import de.instinct.engine.map.GameMap;
import de.instinct.engine.model.AiPlayer;
import de.instinct.engine.model.PlanetData;
import de.instinct.engine.model.Player;
import de.instinct.engine.model.ship.Defense;
import de.instinct.engine.model.ship.Weapon;
import de.instinct.engine.model.ship.WeaponType;
import de.instinct.game.service.model.GameSession;
import de.instinct.game.service.model.User;

public class GameDataLoader {
	
	private static final String MAP_FILE_SUBFOLDER = "maps";
	private static final String MAP_FILE_POSTFIX = ".map";
	
	private AiEngine aiEngine;
	
	public GameDataLoader() {
		aiEngine = new AiEngine();
	}
	
	public GameStateInitialization generateGameStateInitialization(GameSession session) {
		GameStateInitialization initialGameState = loadInitialMap(session);
		initialGameState.gameUUID = session.getUuid();
		initialGameState.players = loadPlayers(session);
		initialGameState.ancientPlanetResourceDegradationFactor = 0.5f;
		initialGameState.gameTimeLimitMS = 180_000;
		initialGameState.atpToWin = 50;
		return initialGameState;
	}

	private GameStateInitialization loadInitialMap(GameSession session) {
		GameStateInitialization initialGameState = new GameStateInitialization();
		
		initialGameState.map = ObjectJSONMapper.mapJSON(FileManager.loadFile(
				MAP_FILE_SUBFOLDER 
				+ "/" + session.getGameType().getGameMode().toString().toLowerCase() 
				+ "/" + session.getGameType().getFactionMode().toString().toLowerCase() 
				+ "/" + session.getGameType().getMap() + MAP_FILE_POSTFIX), GameMap.class);
		return initialGameState;
	}
	
	public GameMap preview(String map) {
		String mapFile = FileManager.loadFile(MAP_FILE_SUBFOLDER + "/conquest/one_vs_one/" + map + MAP_FILE_POSTFIX);
		if (mapFile == null || mapFile.isEmpty()) return null;
		return ObjectJSONMapper.mapJSON(mapFile, GameMap.class);
	}

	public List<Player> loadPlayers(GameSession session) {
		List<Player> players = new ArrayList<>();
		
		Player neutralPlayer = new Player();
		neutralPlayer.id = 0;
		neutralPlayer.teamId = 0;
		neutralPlayer.name = "Neutral Player";
		PlanetData neutralPlanetData = new PlanetData();
		Weapon neutralPlanetWeapon = new Weapon();
		neutralPlanetWeapon.type = WeaponType.PROJECTILE;
		neutralPlanetWeapon.damage = 4;
		neutralPlanetWeapon.range = 100f;
		neutralPlanetWeapon.cooldown = 1000;
		neutralPlanetWeapon.speed = 120f;
		neutralPlanetData.weapon = neutralPlanetWeapon;
		Defense neutralPlanetDefense = new Defense();
		neutralPlanetDefense.armor = 100;
		neutralPlanetData.defense = neutralPlanetDefense;
		neutralPlayer.planetData = neutralPlanetData;
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
				AiPlayer aiPlayer = aiEngine.initialize(AiDifficulty.RETARDED, session.getGameType().getThreatLevel());
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
		newPlayer.commandPointsGenerationSpeed = user.getLoadout().getCommandPointsGenerationSpeed();
		newPlayer.startCommandPoints = user.getLoadout().getStartCommandPoints();
		newPlayer.maxCommandPoints = user.getLoadout().getMaxCommandPoints();
		newPlayer.planetData = EngineInterface.getPlanetData(user.getLoadout().getInfrastructure());
		newPlayer.ships = EngineInterface.getShips(user.getLoadout().getShips(), API.shipyard().shipyard());
		return newPlayer;
	}
	
}
