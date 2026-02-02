package de.instinct.engine_api.ai.service;

import java.util.ArrayList;

import de.instinct.engine.model.AiPlayer;
import de.instinct.engine.model.planet.PlanetData;
import de.instinct.engine.model.ship.ShipData;
import de.instinct.engine.model.ship.components.CoreData;
import de.instinct.engine.model.ship.components.EngineData;
import de.instinct.engine.model.ship.components.types.CoreType;
import de.instinct.engine.model.ship.components.types.EngineType;
import de.instinct.engine.model.turret.PlatformData;
import de.instinct.engine.model.turret.PlatformType;
import de.instinct.engine.model.turret.TurretData;
import de.instinct.engine_api.ai.model.AiStatManager;

public class AIPlayerLoader {
	
	private DifficultyBehaviorLoader difficultyLoader;
	
	public AIPlayerLoader() {
		difficultyLoader = new DifficultyBehaviorLoader();
	}
	
	public AiPlayer initialize(int threatLevel) {
		AiPlayer newAiPlayer = new AiPlayer();
		newAiPlayer.ships = new ArrayList<>();
		newAiPlayer.ships.add(createAIShip(threatLevel));
		
		newAiPlayer.turrets = new ArrayList<>();
		if (threatLevel >= 10) {
			newAiPlayer.turrets.add(createAITurret(threatLevel));
		}
		
		PlanetData aiPlanetData = new PlanetData();
		aiPlanetData.resourceGenerationSpeed = AiStatManager.getResourceGenerationSpeed(threatLevel);
		aiPlanetData.maxResourceCapacity = AiStatManager.getMaxResourceCapacity(threatLevel);
		
		newAiPlayer.planetData = aiPlanetData;
		newAiPlayer.commandPointsGenerationSpeed = AiStatManager.getCommandPointsGenerationSpeed(threatLevel);
		newAiPlayer.startCommandPoints = AiStatManager.getStartCommandPoints(threatLevel);
		newAiPlayer.maxCommandPoints = AiStatManager.getMaxCommandPoints(threatLevel);
		newAiPlayer.currentCommandPoints = newAiPlayer.startCommandPoints;
		
		difficultyLoader.load(newAiPlayer, threatLevel);
		newAiPlayer.name = "AI (" + newAiPlayer.difficulty.toString() + ")";
		return newAiPlayer;
	}

	private ShipData createAIShip(int threatLevel) {
		ShipData aiShip = new ShipData();
		aiShip.resourceCost = 5;
		aiShip.cpCost = 1;
		aiShip.model = "hawk";
		
		CoreData aiShipCore = new CoreData();
		aiShipCore.type = CoreType.FIGHTER;
		aiShip.core = aiShipCore;
		
		EngineData aiShipEngine = new EngineData();
		aiShipEngine.type = EngineType.ION;
		aiShipEngine.speed = AiStatManager.getMovementSpeed(threatLevel);
		aiShipEngine.acceleration = 1f;
		aiShip.engine = aiShipEngine;
		
		aiShip.weapons = new ArrayList<>();
		aiShip.weapons.add(AiStatManager.getShipWeapon(threatLevel));
		
		aiShip.shields = new ArrayList<>();
		if (threatLevel >= 10) {
			aiShip.shields.add(AiStatManager.getShipShield(threatLevel));
		}
		
		aiShip.hull = AiStatManager.getShipHull(threatLevel);
		return aiShip;
	}
	
	private TurretData createAITurret(int threatLevel) {
		TurretData aiTurret = new TurretData();
		aiTurret.resourceCost = 10;
		aiTurret.cpCost = 1;
		aiTurret.model = "projectile";
		
		PlatformData aiTurretPlatform = new PlatformData();
		aiTurretPlatform.rotationSpeed = 1f;
		aiTurretPlatform.type = PlatformType.SERVO;
		aiTurret.platform = aiTurretPlatform;
		
		aiTurret.weapons = new ArrayList<>();
		aiTurret.weapons.add(AiStatManager.getTurretWeapon(threatLevel));
		
		aiTurret.shields = new ArrayList<>();
		aiTurret.shields.add(AiStatManager.getTurretShield(threatLevel));
		
		aiTurret.hull = AiStatManager.getTurretHull(threatLevel);
		return aiTurret;
	}

}
