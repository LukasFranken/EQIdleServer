package de.instinct.engine_api.core.service;

import java.util.ArrayList;
import java.util.List;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.PlanetTurretBlueprint;
import de.instinct.api.construction.dto.PlayerInfrastructure;
import de.instinct.api.construction.dto.PlayerTurretData;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ship.PlayerShipComponentLevel;
import de.instinct.api.shipyard.dto.ship.PlayerShipData;
import de.instinct.api.shipyard.dto.ship.ShipBlueprint;
import de.instinct.api.shipyard.dto.ship.ShipComponent;
import de.instinct.api.shipyard.dto.ship.ShipCore;
import de.instinct.api.shipyard.dto.ship.ShipEngine;
import de.instinct.api.shipyard.dto.ship.ShipHull;
import de.instinct.api.shipyard.dto.ship.ShipShield;
import de.instinct.api.shipyard.dto.ship.ShipWeapon;
import de.instinct.api.shipyard.dto.ship.component.ComponentAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.CoreAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.EngineAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.HullAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.ShieldAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.WeaponAttribute;
import de.instinct.api.shipyard.dto.ship.component.level.CoreLevel;
import de.instinct.api.shipyard.dto.ship.component.level.EngineLevel;
import de.instinct.api.shipyard.dto.ship.component.level.HullLevel;
import de.instinct.api.shipyard.dto.ship.component.level.ShieldLevel;
import de.instinct.api.shipyard.dto.ship.component.level.WeaponLevel;
import de.instinct.api.shipyard.dto.ship.component.types.core.CoreAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.engine.EngineAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.hull.HullAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.shield.ShieldAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.weapon.WeaponAttributeType;
import de.instinct.engine.model.planet.PlanetData;
import de.instinct.engine.model.ship.ShipData;
import de.instinct.engine.model.ship.components.CoreData;
import de.instinct.engine.model.ship.components.EngineData;
import de.instinct.engine.model.ship.components.HullData;
import de.instinct.engine.model.ship.components.ShieldData;
import de.instinct.engine.model.ship.components.WeaponData;
import de.instinct.engine.model.ship.components.types.CoreType;
import de.instinct.engine.model.ship.components.types.EngineType;
import de.instinct.engine.model.ship.components.types.HullType;
import de.instinct.engine.model.ship.components.types.ShieldType;
import de.instinct.engine.model.ship.components.types.WeaponType;
import de.instinct.engine.model.turret.PlatformData;
import de.instinct.engine.model.turret.TurretData;

public class EngineDataInterface {
	
	public static PlanetData getPlanetData(PlayerInfrastructure playerInfrastructure, Infrastructure infrastructure) {
		if (infrastructure != null) {
			PlanetData planetData = new PlanetData();
			planetData.maxResourceCapacity = playerInfrastructure.getMaxResourceCapacity();
			planetData.resourceGenerationSpeed = playerInfrastructure.getResourceGenerationSpeed();
			return planetData;
		}
		return null;
	}

	public static List<TurretData> getPlayerTurretData(PlayerTurretData currentPlayerTurret, Infrastructure infrastructure) {
		List<TurretData> turrets = new ArrayList<>();
		for (PlanetTurretBlueprint planetTurretBlueprint : infrastructure.getTurretBlueprints()) {
			if (planetTurretBlueprint.getId() == currentPlayerTurret.getTurretId()) {
				TurretData turretData = new TurretData();
				turretData.model = planetTurretBlueprint.getName();
				turretData.resourceCost = planetTurretBlueprint.getCost();
				turretData.cpCost = planetTurretBlueprint.getCommandPointsCost();
				
				PlatformData platform = new PlatformData();
				platform.rotationSpeed = planetTurretBlueprint.getRotationSpeed();
				turretData.platform = platform;
				
				HullData hull = new HullData();
				hull.strength = planetTurretBlueprint.getPlanetDefense().getArmor();
				turretData.hull = hull;
				
				turretData.weapons = new ArrayList<>();
				turretData.shields = new ArrayList<>();
				
				turrets.add(turretData);
			}
		}
		return turrets;
	}
	
	public static List<ShipData> getShips(List<PlayerShipData> playerShips, ShipyardData shipyardData) {
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
	            shipData.model = shipBlueprint.getModel();
	            shipData.shields = new ArrayList<>();
	            shipData.weapons = new ArrayList<>();
	            for (ShipComponent component : shipBlueprint.getComponents()) {
	                if (component instanceof ShipCore) {
	                    processCore(shipData, (ShipCore) component, playerShip);
	                } else if (component instanceof ShipEngine) {
	                    processEngine(shipData, (ShipEngine) component, playerShip);
	                } else if (component instanceof ShipHull) {
	                    processHull(shipData, (ShipHull) component, playerShip);
	                } else if (component instanceof ShipShield) {
	                    processShield(shipData, (ShipShield) component, playerShip);
	                } else if (component instanceof ShipWeapon) {
	                    processWeapon(shipData, (ShipWeapon) component, playerShip);
	                }
	            }
	            return shipData;
	        }
	    }
	    return null;
	}

	private static void processCore(ShipData shipData, ShipCore shipCore, PlayerShipData playerShip) {
	    CoreData core = new CoreData();
	    core.id = shipCore.getId();
	    core.type = CoreType.valueOf(shipCore.getType().toString());
	    CoreLevel currentCoreLevel = findLevel(shipCore, playerShip);
	    if (currentCoreLevel != null) {
	        for (ComponentAttribute attribute : currentCoreLevel.getAttributes()) {
	            CoreAttribute coreAttribute = (CoreAttribute) attribute;
	            if (coreAttribute.getType() == CoreAttributeType.CP_COST) {
	                shipData.cpCost = (int) attribute.getValue();
	            }
	            if (coreAttribute.getType() == CoreAttributeType.RESOURCE_COST) {
	                shipData.resourceCost = (float) attribute.getValue();
	            }
	        }
	    }
	    shipData.core = core;
	}

	private static void processEngine(ShipData shipData, ShipEngine shipEngine, PlayerShipData playerShip) {
	    EngineData engine = new EngineData();
	    engine.id = shipEngine.getId();
	    engine.type = EngineType.valueOf(shipEngine.getType().toString());
	    EngineLevel currentEngineLevel = findLevel(shipEngine, playerShip);
	    if (currentEngineLevel != null) {
	        for (ComponentAttribute attribute : currentEngineLevel.getAttributes()) {
	            EngineAttribute engineAttribute = (EngineAttribute) attribute;
	            if (engineAttribute.getType() == EngineAttributeType.SPEED) {
	                engine.speed = (float) attribute.getValue();
	            }
	            if (engineAttribute.getType() == EngineAttributeType.ACCELERATION) {
	                engine.acceleration = (float) attribute.getValue();
	            }
	        }
	    }
	    shipData.engine = engine;
	}

	private static void processHull(ShipData shipData, ShipHull shipHull, PlayerShipData playerShip) {
	    HullData hull = new HullData();
	    hull.id = shipHull.getId();
	    hull.type = HullType.valueOf(shipHull.getType().toString());
	    HullLevel currentHullLevel = findLevel(shipHull, playerShip);
	    if (currentHullLevel != null) {
	        for (ComponentAttribute attribute : currentHullLevel.getAttributes()) {
	            HullAttribute hullAttribute = (HullAttribute) attribute;
	            if (hullAttribute.getType() == HullAttributeType.STRENGTH) {
	                hull.strength = (float) attribute.getValue();
	            }
	            if (hullAttribute.getType() == HullAttributeType.REPAIR_SPEED) {
	                hull.repairSpeed = (float) attribute.getValue();
	            }
	        }
	    }
	    shipData.hull = hull;
	}

	private static void processShield(ShipData shipData, ShipShield shipShield, PlayerShipData playerShip) {
	    ShieldData shield = new ShieldData();
	    shield.id = shipShield.getId();
	    shield.type = ShieldType.valueOf(shipShield.getType().toString());
	    ShieldLevel currentShieldLevel = findLevel(shipShield, playerShip);
	    if (currentShieldLevel != null) {
	        for (ComponentAttribute attribute : currentShieldLevel.getAttributes()) {
	            ShieldAttribute shieldAttribute = (ShieldAttribute) attribute;
	            if (shieldAttribute.getType() == ShieldAttributeType.STRENGTH) {
	                shield.strength = (float) attribute.getValue();
	            }
	            if (shieldAttribute.getType() == ShieldAttributeType.GENERATION) {
	                shield.generation = (float) attribute.getValue();
	            }
	        }
	    }
	    shipData.shields.add(shield);
	}

	private static void processWeapon(ShipData shipData, ShipWeapon shipWeapon, PlayerShipData playerShip) {
	    WeaponData weapon = new WeaponData();
	    weapon.id = shipWeapon.getId();
	    weapon.type = WeaponType.valueOf(shipWeapon.getType().toString());
	    WeaponLevel currentWeaponLevel = findLevel(shipWeapon, playerShip);
	    if (currentWeaponLevel != null) {
	        for (ComponentAttribute attribute : currentWeaponLevel.getAttributes()) {
	            WeaponAttribute weaponAttribute = (WeaponAttribute) attribute;
	            if (weaponAttribute.getType() == WeaponAttributeType.DAMAGE) {
	                weapon.damage = (float) attribute.getValue();
	            }
	            if (weaponAttribute.getType() == WeaponAttributeType.COOLDOWN) {
	                weapon.cooldown = (long) attribute.getValue();
	            }
	            if (weaponAttribute.getType() == WeaponAttributeType.EXPLOSION) {
	                weapon.aoeRadius = (float) attribute.getValue();
	            }
	            if (weaponAttribute.getType() == WeaponAttributeType.RANGE) {
	                weapon.range = (float) attribute.getValue();
	            }
	            if (weaponAttribute.getType() == WeaponAttributeType.SPEED) {
	                weapon.speed = (float) attribute.getValue();
	            }
	        }
	    }
	    shipData.weapons.add(weapon);
	}

	@SuppressWarnings("unchecked")
	private static <T> T findLevel(ShipComponent component, PlayerShipData playerShip) {
	    for (PlayerShipComponentLevel componentLevel : playerShip.getComponentLevels()) {
	        if (componentLevel.getComponentId() == component.getId()) {
	            return (T) component.getLevels().get(componentLevel.getLevel());
	        }
	    }
	    return null;
	}

}
