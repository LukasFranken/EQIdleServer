package de.instinct.game.service.impl;

import java.util.ArrayList;
import java.util.List;

import base.file.FileManager;
import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.PlanetDefense;
import de.instinct.api.construction.dto.PlanetTurretBlueprint;
import de.instinct.api.construction.dto.PlanetWeapon;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.api.shipyard.dto.ShipBlueprint;
import de.instinct.api.shipyard.dto.ShipDefense;
import de.instinct.api.shipyard.dto.ShipWeapon;
import de.instinct.engine.ai.AiDifficulty;
import de.instinct.engine.ai.AiEngine;
import de.instinct.engine.initialization.GameStateInitialization;
import de.instinct.engine.map.GameMap;
import de.instinct.engine.model.AiPlayer;
import de.instinct.engine.model.PlanetData;
import de.instinct.engine.model.Player;
import de.instinct.engine.model.ship.Defense;
import de.instinct.engine.model.ship.ShipData;
import de.instinct.engine.model.ship.ShipType;
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
		initialGameState.map = ObjectJSONMapper.mapJSON(FileManager.loadFile(MAP_FILE_SUBFOLDER + "/" + session.getGameType().map + MAP_FILE_POSTFIX), GameMap.class);
		return initialGameState;
	}
	
	public GameMap preview(String map) {
		return ObjectJSONMapper.mapJSON(FileManager.loadFile(MAP_FILE_SUBFOLDER + "/" + map + MAP_FILE_POSTFIX), GameMap.class);
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
		
		if (session.getGameType().versusMode == VersusMode.AI) {
			for (int i = 0; i < session.getGameType().factionMode.teamPlayerCount; i++) {
				AiPlayer aiPlayer = aiEngine.initialize(AiDifficulty.RETARDED);
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

	private Player getPlayer(User user) {
		Player newPlayer = new Player();
		newPlayer.teamId = user.getTeamid();
		newPlayer.name = user.getName();
		newPlayer.commandPointsGenerationSpeed = user.getLoadout().getCommandPointsGenerationSpeed();
		newPlayer.startCommandPoints = user.getLoadout().getStartCommandPoints();
		newPlayer.maxCommandPoints = user.getLoadout().getMaxCommandPoints();
		newPlayer.planetData = getPlanetData(user);
		newPlayer.ships = getShips(user);
		return newPlayer;
	}

	private PlanetData getPlanetData(User user) {
		Infrastructure infrastructure = user.getLoadout().getInfrastructure();
		if (user.getLoadout().getInfrastructure() != null) {
			PlanetData planetData = new PlanetData();
			planetData.maxResourceCapacity = infrastructure.getMaxResourceCapacity();
			planetData.resourceGenerationSpeed = infrastructure.getResourceGenerationSpeed();
			planetData.percentOfArmorAfterCapture = infrastructure.getPercentOfArmorAfterCapture();
			
			PlanetTurretBlueprint planetTurretBlueprint = null;
			for (PlanetTurretBlueprint currentPlanetTurretBlueprint : infrastructure.getPlanetTurretBlueprints()) {
				if (currentPlanetTurretBlueprint.isInUse()) {
					planetTurretBlueprint = currentPlanetTurretBlueprint;
					break;
				}
			}
			planetData.defense = getDefense(planetTurretBlueprint.getPlanetDefense());
			planetData.weapon = getWeapon(planetTurretBlueprint.getPlanetWeapon());
			return planetData;
		}
		return null;
	}

	private List<ShipData> getShips(User user) {
		List<ShipData> ships = new ArrayList<>();
		for (ShipBlueprint userShip : user.getLoadout().getShips()) {
			ShipData shipData = new ShipData();
			shipData.type = ShipType.valueOf(userShip.getType().toString());
			shipData.cost = userShip.getCost();
			shipData.model = userShip.getModel();
			shipData.movementSpeed = userShip.getMovementSpeed();
			shipData.commandPointsCost = userShip.getCommandPointsCost();
			shipData.weapon = getWeapon(userShip.getWeapon());
			shipData.defense = getDefense(userShip.getDefense());
			ships.add(shipData);
		}
		return ships;
	}
	
	private Defense getDefense(ShipDefense shipDefense) {
		Defense defense = new Defense();
		defense.armor = shipDefense.getArmor();
		defense.shield = shipDefense.getShield();
		defense.shieldRegenerationSpeed = shipDefense.getShieldRegenerationSpeed();
		return defense;
	}

	private Weapon getWeapon(ShipWeapon shipWeapon) {
		Weapon weapon = new Weapon();
		weapon.type = WeaponType.valueOf(shipWeapon.getType().toString());
		weapon.damage = shipWeapon.getDamage();
		weapon.range = shipWeapon.getRange();
		weapon.cooldown = shipWeapon.getCooldown();
		weapon.speed = shipWeapon.getSpeed();
		return weapon;
	}

	private Weapon getWeapon(PlanetWeapon planetWeapon) {
		Weapon weapon = new Weapon();
		weapon.type = WeaponType.valueOf(planetWeapon.getType().toString());
		weapon.damage = planetWeapon.getDamage();
		weapon.range = planetWeapon.getRange();
		weapon.cooldown = planetWeapon.getCooldown();
		weapon.speed = planetWeapon.getSpeed();
		return weapon;
	}

	private Defense getDefense(PlanetDefense planetDefense) {
		Defense defense = new Defense();
		defense.armor = planetDefense.getArmor();
		defense.shield = planetDefense.getShield();
		defense.shieldRegenerationSpeed = planetDefense.getShieldRegenerationSpeed();
		return defense;
	}
	
}
