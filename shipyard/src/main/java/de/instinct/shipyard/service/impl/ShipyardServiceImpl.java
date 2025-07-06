package de.instinct.shipyard.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import base.file.FileManager;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.shipyard.dto.ShipBlueprint;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.StatChangeResponse;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
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
		ShipyardData shipyardData = ObjectJSONMapper.mapJSON(FileManager.loadFile("init.data"), ShipyardData.class);
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
		if (shipyard.getActiveShipSlots() > 1 && shipyard.getActiveShipSlots() <= getActiveShips(shipyard)) return UseShipResponseCode.NO_ACTIVE_SLOTS_AVAILABLE;
		if (ship.isInUse()) return UseShipResponseCode.ALREADY_IN_USE;
		if (shipyard.getActiveShipSlots() == 1) {
			for (ShipBlueprint blueprint : shipyard.getOwnedShips()) {
				blueprint.setInUse(false);
			}
		}
		ship.setInUse(true);
		return UseShipResponseCode.SUCCESS;
	}
	
	@Override
	public UnuseShipResponseCode unuseShip(String token, String shipUUID) {
		ShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) return UnuseShipResponseCode.NOT_INITIALIZED;
		ShipBlueprint ship = shipyard.getOwnedShips().stream()
				.filter(s -> s.getUuid().equals(shipUUID))
				.findFirst()
				.orElse(null);
		if (ship == null) return UnuseShipResponseCode.INVALID_UUID;
		if (!ship.isInUse()) return UnuseShipResponseCode.NOT_IN_USE;
		ship.setInUse(false);
		return UnuseShipResponseCode.SUCCESS;
	}

	private int getActiveShips(ShipyardData shipyard) {
		return shipyard.getOwnedShips().stream()
				.filter(ShipBlueprint::isInUse)
				.toList()
				.size();
	}

	@Override
	public StatChangeResponse changeHangarSpace(String token, int count) {
		ShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) return StatChangeResponse.INVALID_TOKEN;
		shipyard.setSlots(shipyard.getSlots() + count);
		return StatChangeResponse.SUCCESS;
	}

	@Override
	public StatChangeResponse changeActiveShips(String token, int count) {
		ShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) return StatChangeResponse.INVALID_TOKEN;
		shipyard.setActiveShipSlots(shipyard.getActiveShipSlots() + count);
		return StatChangeResponse.SUCCESS;
	}

	
	
}
