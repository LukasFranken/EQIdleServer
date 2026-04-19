package de.instinct.engine_api.fleet.ai.service;

import java.util.ArrayList;

import de.instinct.engine.fleet.ai.data.AiPlayer;
import de.instinct.engine.fleet.entity.planet.data.PlanetData;
import de.instinct.engine.fleet.entity.unit.component.data.types.HullType;
import de.instinct.engine.fleet.entity.unit.ship.component.types.CoreType;
import de.instinct.engine.fleet.entity.unit.ship.component.types.EngineType;
import de.instinct.engine.fleet.entity.unit.ship.data.ShipData;
import de.instinct.engine.fleet.entity.unit.turret.data.TurretData;
import de.instinct.engine_api.fleet.ai.model.AiStatManager;

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
		newAiPlayer.turrets.add(createAITurret(threatLevel));
		
		PlanetData aiPlanetData = new PlanetData();
		aiPlanetData.turretSlots = 1;
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
		aiShip.transferRate = 0.5f;
		
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
		
		aiTurret.weapons = new ArrayList<>();
		if (threatLevel >= 20) {
			aiTurret.weapons.add(AiStatManager.getTurretWeapon(threatLevel));
		}
		
		aiTurret.shields = new ArrayList<>();
		aiTurret.shields.add(AiStatManager.getTurretShield(threatLevel));
		
		aiTurret.hullType = HullType.ALLOY;
		aiTurret.hullStrength = AiStatManager.getTurretHullStrength(threatLevel);
		return aiTurret;
	}

}
