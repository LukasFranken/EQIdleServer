package de.instinct.engine_api.fleet.ai.model;

import de.instinct.engine.fleet.entity.unit.component.data.ShieldData;
import de.instinct.engine.fleet.entity.unit.component.data.WeaponData;
import de.instinct.engine.fleet.entity.unit.component.data.types.ShieldType;
import de.instinct.engine.fleet.entity.unit.component.data.types.WeaponType;

public class AiStatManager {
	
	private static float descaleFactor = 20f;

	public static float getMovementSpeed(int threatLevel) {
		float maxMoveSpeed = 100f;
		float minMoveSpeed = 30f;
		return Math.min(minMoveSpeed + minMoveSpeed * (threatLevel/50), maxMoveSpeed);
	}

	public static WeaponData getShipWeapon(int threatLevel) {
		WeaponData aiShipWeapon = new WeaponData();
		aiShipWeapon.type = WeaponType.LASER;
		aiShipWeapon.damage = 2 + 3 * (threatLevel / descaleFactor);
		
		float maxRange = 100f;
		float minRange = 50f;
		aiShipWeapon.range = Math.min(minRange + minRange * (threatLevel/(descaleFactor * 10f)), maxRange);
		
		float maxCooldown = 1000;
		float minCooldown = 300;
		aiShipWeapon.cooldown = (int)Math.max(maxCooldown - maxCooldown * (threatLevel/(descaleFactor * 10f)), minCooldown);
		
		float maxSpeed = 100;
		float minSpeed = 40;
		aiShipWeapon.speed = Math.min(minSpeed + minSpeed * (threatLevel/(descaleFactor * 10f)), maxSpeed);
		return aiShipWeapon;
	}

	public static ShieldData getShipShield(int threatLevel) {
		ShieldData aiShipShield = new ShieldData();
		aiShipShield.type = ShieldType.PLASMA;
		aiShipShield.strength = 2 + 5 * (threatLevel / descaleFactor);
		aiShipShield.generation = 0.1f + 0.3f * (threatLevel / descaleFactor);
		return aiShipShield;
	}
	
	public static float getShipHullStrength(int threatLevel) {
		return 3 + 8 * (threatLevel / descaleFactor);
	}

	public static WeaponData getTurretWeapon(int threatLevel) {
		WeaponData aiPlanetWeapon = new WeaponData();
		aiPlanetWeapon.type = WeaponType.LASER;
		aiPlanetWeapon.damage = 2 + 2 * (threatLevel / descaleFactor);
		
		float maxRange = 150f;
		float minRange = 100f;
		aiPlanetWeapon.range = Math.min(minRange + minRange * (threatLevel/(descaleFactor * 10f)), maxRange);
		
		float maxCooldown = 1000;
		float minCooldown = 600;
		aiPlanetWeapon.cooldown = (int)Math.max(maxCooldown - maxCooldown * (threatLevel/(descaleFactor * 10f)), minCooldown);
		
		float maxSpeed = 100;
		float minSpeed = 40;
		aiPlanetWeapon.speed = Math.min(minSpeed + minSpeed * (threatLevel/(descaleFactor * 10f)), maxSpeed);
		return aiPlanetWeapon;
	}
	
	public static ShieldData getTurretShield(int threatLevel) {
		ShieldData aiTurretShield = new ShieldData();
		aiTurretShield.type = ShieldType.PLASMA;
		aiTurretShield.strength = 3 + 15 * (threatLevel / descaleFactor);
		aiTurretShield.generation = 0.2f + 0.5f * (threatLevel / descaleFactor);
		return aiTurretShield;
	}
	
	public static float getTurretHullStrength(int threatLevel) {
		return 5 + 20 * (threatLevel / descaleFactor);
	}

	public static float getBaseResourceGenerationSpeed(int threatLevel) {
		float resourceGenerationSpeedPerPowOfTen = 0.1f;
		return scaleWithThreatLevelByPowerOfTen(threatLevel, resourceGenerationSpeedPerPowOfTen);
	}
	
	public static float getPlanetBaseResourceGenerationSpeed(int threatLevel) {
		float resourceGenerationSpeedPerPowOfTen = 0.1f;
		return scaleWithThreatLevelByPowerOfTen(threatLevel, resourceGenerationSpeedPerPowOfTen);
	}

	public static float getMaxResourceCapacity(int threatLevel) {
		float maxResourceCapacityPerPowOfTen = 10f;
		return scaleWithThreatLevelByPowerOfTen(threatLevel, maxResourceCapacityPerPowOfTen);
	}
	
	public static float getStartResources(int threatLevel) {
		float startCommandPointsPerPowOfTen = 3f;
		return scaleWithThreatLevelByPowerOfTen(threatLevel, startCommandPointsPerPowOfTen);
	}
	
	private static float scaleWithThreatLevelByPowerOfTen(int threatLevel, float baseValue) {
		float remainingThreat = threatLevel;
		float scalingMaxResourceCapacity = 0;
		while (remainingThreat > 10) {
			remainingThreat /= 10;
			scalingMaxResourceCapacity += baseValue;
		}
		scalingMaxResourceCapacity += (remainingThreat/10f) * baseValue;
		return Math.max(baseValue, scalingMaxResourceCapacity);
	}

}
