package de.instinct.shipyard.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.api.meta.dto.ShipData;
import de.instinct.api.meta.dto.ShipType;
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
		List<ShipData> ownedShips = new ArrayList<>();
		ownedShips.add(ShipData.builder()
				.uuid(UUID.randomUUID().toString())
				.type(ShipType.FIGHTER)
				.model("hawk")
				.movementSpeed(120f)
				.cost(3)
				.health(2)
				.power(5)
				.inUse(true)
				.build());
		ownedShips.add(ShipData.builder()
				.uuid(UUID.randomUUID().toString())
				.type(ShipType.FIGHTER)
				.model("turtle")
				.movementSpeed(50f)
				.cost(6)
				.health(10)
				.power(5)
				.build());
		ownedShips.add(ShipData.builder()
				.uuid(UUID.randomUUID().toString())
				.type(ShipType.FIGHTER)
				.model("shark")
				.movementSpeed(75f)
				.cost(5)
				.health(7)
				.power(8)
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
		return userShipyards.get(token);
	}

	@Override
	public UseShipResponseCode useShip(String token, String shipUUID) {
		ShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) return UseShipResponseCode.NOT_INITIALIZED;
		ShipData ship = shipyard.getOwnedShips().stream()
				.filter(s -> s.getUuid().equals(shipUUID))
				.findFirst()
				.orElse(null);
		if (ship == null) return UseShipResponseCode.INVALID_UUID;
		//if (shipyard.getSlots() <= 0) return UseShipResponseCode.NO_SLOTS_AVAILABLE;
		if (ship.isInUse()) return UseShipResponseCode.ALREADY_IN_USE;
		for (ShipData s : shipyard.getOwnedShips()) {
			s.setInUse(false);
		}
		ship.setInUse(true);
		return UseShipResponseCode.SUCCESS;
	}

	
	
}
