package de.instinct.engine_api.ai.service;

import java.util.ArrayList;

import de.instinct.engine.model.Player;
import de.instinct.engine.model.planet.PlanetData;
import de.instinct.engine.model.ship.components.HullData;
import de.instinct.engine.model.ship.components.ShieldData;
import de.instinct.engine.model.ship.components.WeaponData;
import de.instinct.engine.model.ship.components.types.ShieldType;
import de.instinct.engine.model.ship.components.types.WeaponType;
import de.instinct.engine.model.turret.PlatformData;
import de.instinct.engine.model.turret.PlatformType;
import de.instinct.engine.model.turret.TurretData;

public class NeutralPlayerLoader {
	
	public Player createNeutralPlayer(int threatLevel) {
	Player neutralPlayer = new Player();
	neutralPlayer.commandPointsGenerationSpeed = 0;
	neutralPlayer.startCommandPoints = 0;
	neutralPlayer.maxCommandPoints = 0;
	PlanetData neutralPlanetData = new PlanetData();
	neutralPlanetData.resourceGenerationSpeed = 0;
	neutralPlanetData.maxResourceCapacity = 0;
	neutralPlayer.planetData = neutralPlanetData;
	
	neutralPlayer.turrets = new ArrayList<>();
	TurretData neutralTurret = new TurretData();
	neutralTurret.model = "projectile";
	neutralTurret.cpCost = 0;
	neutralTurret.resourceCost = 0;
	
	PlatformData neutralTurretPlatform = new PlatformData();
	neutralTurretPlatform.type = PlatformType.SERVO;
	neutralTurretPlatform.rotationSpeed = 1f;
	neutralTurret.platform = neutralTurretPlatform;
	
	HullData neutralTurretHull = new HullData();
	neutralTurretHull.strength = 5 + threatLevel;
	neutralTurret.hull = neutralTurretHull;
	
	neutralTurret.weapons = new ArrayList<>();
	WeaponData neutralTurretWeapon = new WeaponData();
	neutralTurretWeapon.type = WeaponType.MISSILE;
	neutralTurretWeapon.damage = 1 + ((float)threatLevel/25f);
	neutralTurretWeapon.range = 100f;
	neutralTurretWeapon.cooldown = 1000;
	neutralTurretWeapon.speed = 120f;
	neutralTurret.weapons.add(neutralTurretWeapon);
	
	neutralTurret.shields = new ArrayList<>();
	if (threatLevel >= 100) {
		ShieldData neutralTurretShield1 = new ShieldData();
		neutralTurretShield1.type = ShieldType.NULLPOINT;
		neutralTurretShield1.strength = 2 + (threatLevel / 100);
		neutralTurretShield1.generation = 0.2f;
		neutralTurret.shields.add(neutralTurretShield1);
	}
	ShieldData neutralTurretShield2 = new ShieldData();
	neutralTurretShield2.type = ShieldType.PLASMA;
	neutralTurretShield2.strength = 10 + (threatLevel / 5);
	neutralTurretShield2.generation = 0.1f + (threatLevel / 50f);
	neutralTurret.shields.add(neutralTurretShield2);
	neutralPlayer.turrets.add(neutralTurret);
	
	neutralPlayer.ships = new ArrayList<>();
	return neutralPlayer;
}

}
