package de.instinct.shipyard.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.api.construction.dto.WeaponType;
import de.instinct.api.shipyard.dto.ShipBlueprint;
import de.instinct.api.shipyard.dto.ShipDefense;
import de.instinct.api.shipyard.dto.ShipType;
import de.instinct.api.shipyard.dto.ShipWeapon;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;
import de.instinct.shipyard.service.ShipyardService;

@Service
public class ShipyardServiceImpl implements ShipyardService {
	
	private Map<String, ShipyardData> userShipyards;
	
	public ShipyardServiceImpl() {
		userShipyards = new HashMap<>();
	}
	
	@Override
	public ShipyardInitializationResponseCode init(String token) {
		if (userShipyards.containsKey(token)) return ShipyardInitializationResponseCode.ALREADY_INITIALIZED;
		List<ShipBlueprint> ownedShips = new ArrayList<>();
		ShipDefense hawkDefense = ShipDefense.builder()
				.shield(2)
				.armor(5)
				.shieldRegenerationSpeed(0.2f)
				.build();
		ShipWeapon hawkWeapon = ShipWeapon.builder()
				.type(WeaponType.LASER)
				.damage(2)
				.range(80f)
				.speed(100f)
				.cooldown(2000)
				.build();
		ownedShips.add(ShipBlueprint.builder()
				.uuid(UUID.randomUUID().toString())
				.type(ShipType.FIGHTER)
				.model("hawk")
				.movementSpeed(50f)
				.cost(3)
				.commandPointsCost(1)
				.defense(hawkDefense)
				.weapon(hawkWeapon)
				.inUse(true)
				.build());
		
		ShipDefense turtleDefense = ShipDefense.builder()
				.shield(5)
				.armor(7)
				.shieldRegenerationSpeed(0.2f)
				.build();
		ShipWeapon turtleWeapon = ShipWeapon.builder()
				.type(WeaponType.PROJECTILE)
				.damage(2)
				.range(40f)
				.speed(100f)
				.cooldown(2000)
				.build();
		ownedShips.add(ShipBlueprint.builder()
				.uuid(UUID.randomUUID().toString())
				.type(ShipType.FIGHTER)
				.model("turtle")
				.movementSpeed(30f)
				.cost(6)
				.commandPointsCost(1)
				.defense(turtleDefense)
				.weapon(turtleWeapon)
				.build());
		
		ShipDefense sharkDefense = ShipDefense.builder()
				.shield(3)
				.armor(3)
				.shieldRegenerationSpeed(0.2f)
				.build();
		ShipWeapon sharkWeapon = ShipWeapon.builder()
				.type(WeaponType.MISSILE)
				.damage(5)
				.range(90f)
				.speed(60f)
				.cooldown(2000)
				.build();
		ownedShips.add(ShipBlueprint.builder()
				.uuid(UUID.randomUUID().toString())
				.type(ShipType.FIGHTER)
				.model("shark")
				.movementSpeed(40f)
				.cost(5)
				.commandPointsCost(1)
				.defense(sharkDefense)
				.weapon(sharkWeapon)
				.build());
		
		ShipDefense cheetahDefense = ShipDefense.builder()
				.shield(5)
				.armor(10)
				.shieldRegenerationSpeed(0.5f)
				.build();
		ShipWeapon cheetahWeapon = ShipWeapon.builder()
				.type(WeaponType.BEAM)
				.damage(5)
				.range(120f)
				.speed(300f)
				.cooldown(3000)
				.build();
		ownedShips.add(ShipBlueprint.builder()
				.uuid(UUID.randomUUID().toString())
				.type(ShipType.FIGHTER)
				.model("cheetah")
				.movementSpeed(120f)
				.cost(2)
				.commandPointsCost(1)
				.defense(cheetahDefense)
				.weapon(cheetahWeapon)
				.inUse(false)
				.build());
		ShipyardData shipyardData = ShipyardData.builder()
				.ownedShips(ownedShips)
				.slots(5)
				.build();
		userShipyards.put(token, shipyardData);
		return ShipyardInitializationResponseCode.SUCCESS;
	}

	@Override
	public ShipyardData getShipyardData(String token) {
		ShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) {
			init(token);
			shipyard = userShipyards.get(token);
		}
		return shipyard;
	}

	@Override
	public UseShipResponseCode useShip(String token, String shipUUID) {
		ShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) return UseShipResponseCode.NOT_INITIALIZED;
		ShipBlueprint ship = shipyard.getOwnedShips().stream()
				.filter(s -> s.getUuid().equals(shipUUID))
				.findFirst()
				.orElse(null);
		if (ship == null) return UseShipResponseCode.INVALID_UUID;
		//if (shipyard.getSlots() <= 0) return UseShipResponseCode.NO_SLOTS_AVAILABLE;
		if (ship.isInUse()) return UseShipResponseCode.ALREADY_IN_USE;
		for (ShipBlueprint s : shipyard.getOwnedShips()) {
			s.setInUse(false);
		}
		ship.setInUse(true);
		return UseShipResponseCode.SUCCESS;
	}

	
	
}
