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
import de.instinct.api.shipyard.service.impl.ShipyardInitializationResponseCode;
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
				.movementSpeed(70f)
				.cost(5)
				.power(5)
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

	
	
}
