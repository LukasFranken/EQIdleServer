package de.instinct.engine_api.fleet.ai.service;

import java.util.ArrayList;

import de.instinct.engine.fleet.entity.planet.data.PlanetData;
import de.instinct.engine.fleet.entity.unit.component.data.ShieldData;
import de.instinct.engine.fleet.entity.unit.component.data.WeaponData;
import de.instinct.engine.fleet.entity.unit.component.data.types.HullType;
import de.instinct.engine.fleet.entity.unit.component.data.types.ShieldType;
import de.instinct.engine.fleet.entity.unit.component.data.types.WeaponType;
import de.instinct.engine.fleet.entity.unit.turret.data.TurretData;
import de.instinct.engine.fleet.player.FleetPlayer;

public class NeutralPlayerLoader {
	
	public FleetPlayer createNeutralPlayer(int threatLevel) {
		FleetPlayer neutralPlayer = new FleetPlayer();
	
	PlanetData neutralPlanetData = new PlanetData();
	neutralPlayer.planetData = neutralPlanetData;
	neutralPlayer.planetData.turretSlots = 1;
	
	neutralPlayer.turrets = new ArrayList<>();
	TurretData neutralTurret = new TurretData();
	neutralTurret.model = "projectile";
	neutralTurret.resourceCost = 0;
	
	neutralTurret.hullType = HullType.ALLOY;
	neutralTurret.hullStrength = 5 + threatLevel;
	
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
