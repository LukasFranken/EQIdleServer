package de.instinct.engine_api.ai.service;

import java.util.ArrayList;

import de.instinct.engine.model.AiPlayer;
import de.instinct.engine.model.planet.PlanetData;
import de.instinct.engine.model.ship.ShipData;
import de.instinct.engine.model.ship.components.types.CoreType;
import de.instinct.engine.model.ship.components.types.EngineType;
import de.instinct.engine.model.ship.components.types.HullType;
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
		if (threatLevel >= 100) {
			newAiPlayer.turrets.add(createAITurret(threatLevel));
		}
		
		PlanetData aiPlanetData = new PlanetData();
		aiPlanetData.baseResourceGenerationSpeed = AiStatManager.getPlanetBaseResourceGenerationSpeed(threatLevel);
		
		newAiPlayer.planetData = aiPlanetData;
		newAiPlayer.resourceGenerationSpeed = AiStatManager.getBaseResourceGenerationSpeed(threatLevel);
		newAiPlayer.startResources = AiStatManager.getStartResources(threatLevel);
		newAiPlayer.maxResources = AiStatManager.getMaxResourceCapacity(threatLevel);
		
		difficultyLoader.load(newAiPlayer, threatLevel);
		newAiPlayer.name = "AI (" + newAiPlayer.difficulty.toString() + ")";
		return newAiPlayer;
	}

	private ShipData createAIShip(int threatLevel) {
		ShipData aiShip = new ShipData();
		aiShip.resourceCost = 3;
		aiShip.model = "hawk";
		aiShip.coreType = CoreType.FIGHTER;
		
		aiShip.engineType = EngineType.ION;
		aiShip.speed = AiStatManager.getMovementSpeed(threatLevel);
		aiShip.acceleration = 1f;
		
		aiShip.weapons = new ArrayList<>();
		aiShip.weapons.add(AiStatManager.getShipWeapon(threatLevel));
		
		aiShip.shields = new ArrayList<>();
		if (threatLevel >= 10) {
			aiShip.shields.add(AiStatManager.getShipShield(threatLevel));
		}
		
		aiShip.hullType = HullType.ALLOY;
		aiShip.hullStrength = AiStatManager.getShipHullStrength(threatLevel);
		return aiShip;
	}
	
	private TurretData createAITurret(int threatLevel) {
		TurretData aiTurret = new TurretData();
		aiTurret.resourceCost = 10;
		aiTurret.model = "projectile";
		
		PlatformData aiTurretPlatform = new PlatformData();
		aiTurretPlatform.type = PlatformType.SERVO;
		aiTurret.platform = aiTurretPlatform;
		
		aiTurret.weapons = new ArrayList<>();
		aiTurret.weapons.add(AiStatManager.getTurretWeapon(threatLevel));
		
		aiTurret.shields = new ArrayList<>();
		aiTurret.shields.add(AiStatManager.getTurretShield(threatLevel));
		
		aiTurret.hullType = HullType.ALLOY;
		aiTurret.hullStrength = AiStatManager.getTurretHullStrength(threatLevel);
		return aiTurret;
	}

}
