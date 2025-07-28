package de.instinct.api.game.engine;

import java.util.ArrayList;
import java.util.List;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.PlanetDefense;
import de.instinct.api.construction.dto.PlanetTurretBlueprint;
import de.instinct.api.construction.dto.PlanetWeapon;
import de.instinct.api.shipyard.dto.PlayerShipData;
import de.instinct.api.shipyard.dto.ShipBlueprint;
import de.instinct.api.shipyard.dto.ShipStat;
import de.instinct.api.shipyard.dto.ShipStatChange;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.engine.model.PlanetData;
import de.instinct.engine.model.ship.Defense;
import de.instinct.engine.model.ship.ShipData;
import de.instinct.engine.model.ship.ShipType;
import de.instinct.engine.model.ship.Weapon;
import de.instinct.engine.model.ship.WeaponType;

public class EngineInterface {
	
	public static PlanetData getPlanetData(Infrastructure infrastructure) {
		if (infrastructure != null) {
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

	private static Weapon getWeapon(PlanetWeapon planetWeapon) {
		Weapon weapon = new Weapon();
		weapon.type = WeaponType.valueOf(planetWeapon.getType().toString());
		weapon.damage = planetWeapon.getDamage();
		weapon.range = planetWeapon.getRange();
		weapon.cooldown = planetWeapon.getCooldown();
		weapon.speed = planetWeapon.getSpeed();
		return weapon;
	}

	private static Defense getDefense(PlanetDefense planetDefense) {
		Defense defense = new Defense();
		defense.armor = planetDefense.getArmor();
		defense.shield = planetDefense.getShield();
		defense.shieldRegenerationSpeed = planetDefense.getShieldRegenerationSpeed();
		return defense;
	}
	
	public static List<ShipData> getShips(List<PlayerShipData> playerShips, ShipyardData shipyardData) {;
		List<ShipData> ships = new ArrayList<>();
		for (PlayerShipData playerShip : playerShips) {
			ships.add(getShipData(playerShip, shipyardData));
		}
		return ships;
	}
	
	public static ShipData getShipData(PlayerShipData playerShip, ShipyardData shipyardData) {
		for (ShipBlueprint shipBlueprint : shipyardData.getShipBlueprints()) {
			if (shipBlueprint.getId() == playerShip.getShipId()) {
				ShipData shipData = new ShipData();
				shipData.type = ShipType.valueOf(shipBlueprint.getType().toString());
				shipData.model = shipBlueprint.getModel();
				shipData.cost = (int)(shipBlueprint.getCost() + getModificationValue(ShipStat.COST, playerShip.getLevel(), shipBlueprint));
				shipData.commandPointsCost = (int)(shipBlueprint.getCommandPointsCost() + getModificationValue(ShipStat.COMMAND_POINTS_COST, playerShip.getLevel(), shipBlueprint));
				shipData.movementSpeed = shipBlueprint.getMovementSpeed() + getModificationValue(ShipStat.MOVEMENT_SPEED, playerShip.getLevel(), shipBlueprint);
				shipData.weapon = getWeapon(shipBlueprint, playerShip.getLevel());
				shipData.defense = getDefense(shipBlueprint, playerShip.getLevel());
				return shipData;
			}
		}
		return null;
	}

	private static float getModificationValue(ShipStat stat, int level, ShipBlueprint shipBlueprint) {
		float modifierValue = 0;
		for (int i = 0; i < shipBlueprint.getLevels().size(); i++) {
			if (i < level) {
				for (ShipStatChange effect : shipBlueprint.getLevels().get(i).getStatEffects()) {
					if (effect.getStat() == stat) {
						modifierValue += effect.getValue();
					}
				}
			}
		}
		return modifierValue;
	}

	private static Defense getDefense(ShipBlueprint shipBlueprint, int level) {
		Defense defense = new Defense();
		defense.armor = shipBlueprint.getDefense().getArmor() + getModificationValue(ShipStat.DEFENSE_ARMOR, level, shipBlueprint);
		defense.shield = shipBlueprint.getDefense().getShield() + getModificationValue(ShipStat.DEFENSE_SHIELD, level, shipBlueprint);
		defense.shieldRegenerationSpeed = shipBlueprint.getDefense().getShieldRegenerationSpeed() + getModificationValue(ShipStat.DEFENSE_SHIELD_REGENERATION_SPEED, level, shipBlueprint);
		return defense;
	}

	private static Weapon getWeapon(ShipBlueprint shipBlueprint, int level) {
		Weapon weapon = new Weapon();
		weapon.type = WeaponType.valueOf(shipBlueprint.getWeapon().getType().toString());
		weapon.damage = shipBlueprint.getWeapon().getDamage() + getModificationValue(ShipStat.WEAPON_DAMAGE, level, shipBlueprint);
		weapon.aoeRadius = shipBlueprint.getWeapon().getAoeRadius() + getModificationValue(ShipStat.WEAPON_AOE_RADIUS, level, shipBlueprint);
		weapon.range = shipBlueprint.getWeapon().getRange() + getModificationValue(ShipStat.WEAPON_RANGE, level, shipBlueprint);
		weapon.cooldown = (int)(shipBlueprint.getWeapon().getCooldown() + getModificationValue(ShipStat.WEAPON_COOLDOWN, level, shipBlueprint));
		weapon.speed = shipBlueprint.getWeapon().getSpeed() + getModificationValue(ShipStat.WEAPON_PROJECTILE_SPEED, level, shipBlueprint);
		return weapon;
	}

}
