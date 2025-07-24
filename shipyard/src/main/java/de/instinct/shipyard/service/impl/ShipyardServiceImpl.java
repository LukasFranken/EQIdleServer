package de.instinct.shipyard.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.api.core.API;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.meta.dto.ResourceAmount;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.shipyard.dto.PlayerShipData;
import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shipyard.dto.ShipAddResponse;
import de.instinct.api.shipyard.dto.ShipBlueprint;
import de.instinct.api.shipyard.dto.ShipBuildResponse;
import de.instinct.api.shipyard.dto.ShipUpgradeResponse;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.StatChangeResponse;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;
import de.instinct.base.file.FileManager;
import de.instinct.shipyard.service.ShipyardService;

@Service
public class ShipyardServiceImpl implements ShipyardService {
	
	private ShipyardData baseShipyardData;
	private Map<String, PlayerShipyardData> userShipyards;
	
	public ShipyardServiceImpl() {
		userShipyards = new HashMap<>();
	}
	
	@Override
	public ShipyardInitializationResponseCode init(String token) {
		if (userShipyards.containsKey(token)) return ShipyardInitializationResponseCode.ALREADY_INITIALIZED;
		List<PlayerShipData> initShips = new ArrayList<>();
		PlayerShipData initShipData = new PlayerShipData();
		initShipData.setUuid(UUID.randomUUID().toString());
		initShipData.setShipId(0);
		initShipData.setBuilt(true);
		initShipData.setInUse(true);
		initShips.add(initShipData);
		
		PlayerShipyardData initShipyardData = new PlayerShipyardData();
		initShipyardData.setSlots(getBaseData().getBaseSlots());
		initShipyardData.setActiveShipSlots(getBaseData().getBaseActiveShipSlots());
		initShipyardData.setShips(initShips);
		userShipyards.put(token, initShipyardData);
		return ShipyardInitializationResponseCode.SUCCESS;
	}

	@Override
	public PlayerShipyardData getShipyardData(String token) {
		PlayerShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) {
			init(token);
			shipyard = userShipyards.get(token);
		}
		return shipyard;
	}
	
	@Override
	public ShipyardData getBaseData() {
		if (baseShipyardData == null) baseShipyardData = ObjectJSONMapper.mapJSON(FileManager.loadFile("init.data"), ShipyardData.class);
		return baseShipyardData;
	}

	@Override
	public UseShipResponseCode useShip(String token, String shipUUID) {
		PlayerShipyardData playerShipyard = userShipyards.get(token);
		if (playerShipyard == null) return UseShipResponseCode.NOT_INITIALIZED;
		PlayerShipData ship = playerShipyard.getShips().stream()
				.filter(s -> s.getUuid().equals(shipUUID))
				.findFirst()
				.orElse(null);
		if (ship == null) return UseShipResponseCode.INVALID_UUID;
		if (playerShipyard.getActiveShipSlots() > 1 && playerShipyard.getActiveShipSlots() <= getActiveShips(playerShipyard)) return UseShipResponseCode.NO_ACTIVE_SLOTS_AVAILABLE;
		if (ship.isInUse()) return UseShipResponseCode.ALREADY_IN_USE;
		if (!ship.isBuilt()) return UseShipResponseCode.NOT_BUILT;
		if (playerShipyard.getActiveShipSlots() == 1) {
			for (PlayerShipData shipData : playerShipyard.getShips()) {
				shipData.setInUse(false);
			}
		}
		ship.setInUse(true);
		return UseShipResponseCode.SUCCESS;
	}
	
	@Override
	public UnuseShipResponseCode unuseShip(String token, String shipUUID) {
		PlayerShipyardData playerShipyard = userShipyards.get(token);
		if (playerShipyard == null) return UnuseShipResponseCode.NOT_INITIALIZED;
		PlayerShipData ship = playerShipyard.getShips().stream()
				.filter(s -> s.getUuid().equals(shipUUID))
				.findFirst()
				.orElse(null);
		if (ship == null) return UnuseShipResponseCode.INVALID_UUID;
		if (!ship.isInUse()) return UnuseShipResponseCode.NOT_IN_USE;
		ship.setInUse(false);
		return UnuseShipResponseCode.SUCCESS;
	}

	private int getActiveShips(PlayerShipyardData playerShipyard) {
		return playerShipyard.getShips().stream()
				.filter(PlayerShipData::isInUse)
				.toList()
				.size();
	}

	@Override
	public StatChangeResponse changeHangarSpace(String token, int count) {
		PlayerShipyardData playerShipyard = userShipyards.get(token);
		if (playerShipyard == null) return StatChangeResponse.INVALID_TOKEN;
		playerShipyard.setSlots(playerShipyard.getSlots() + count);
		return StatChangeResponse.SUCCESS;
	}

	@Override
	public StatChangeResponse changeActiveShips(String token, int count) {
		PlayerShipyardData playerShipyard = userShipyards.get(token);
		if (playerShipyard == null) return StatChangeResponse.INVALID_TOKEN;
		playerShipyard.setActiveShipSlots(playerShipyard.getActiveShipSlots() + count);
		return StatChangeResponse.SUCCESS;
	}

	@Override
	public ShipBuildResponse build(String token, String shiptoken) {
		PlayerShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) return ShipBuildResponse.USER_DOESNT_EXIST;
		PlayerShipData ship = shipyard.getShips().stream()
				.filter(s -> s.getUuid().contentEquals(shiptoken))
				.findFirst()
				.orElse(null);
		if (ship == null) return ShipBuildResponse.SHIP_DOESNT_EXIST;
		if (ship.isBuilt()) return ShipBuildResponse.ALREADY_BUILT;
		ShipBlueprint blueprint = getBaseData().getShipBlueprints().stream()
				.filter(bp -> bp.getId() == ship.getShipId())
				.findFirst()
				.orElse(null);
		if (blueprint == null) return ShipBuildResponse.BLUEPRINT_NOT_FOUND;
		ResourceData playerResources = API.meta().resources(token);
		for (ResourceAmount resourceCost : blueprint.getBuildCost()) {
			if (!playerResources.contains(resourceCost)) {
				return ShipBuildResponse.NOT_ENOUGH_RESOURCES;
			}
		}
		ResourceData resourceUpdate = new ResourceData();
		resourceUpdate.setResources(blueprint.getBuildCost());
		API.meta().addResources(token, resourceUpdate);
		
		ship.setBuilt(true);
		return ShipBuildResponse.SUCCESS;
	}

	@Override
	public ShipUpgradeResponse upgrade(String token, String shiptoken) {
		PlayerShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) return ShipUpgradeResponse.USER_DOESNT_EXIST;
		PlayerShipData ship = shipyard.getShips().stream()
				.filter(s -> s.getUuid().contentEquals(shiptoken))
				.findFirst()
				.orElse(null);
		if (ship == null) return ShipUpgradeResponse.SHIP_DOESNT_EXIST;
		if (!ship.isBuilt()) return ShipUpgradeResponse.NOT_BUILT;
		ShipBlueprint blueprint = getBaseData().getShipBlueprints().stream()
				.filter(bp -> bp.getId() == ship.getShipId())
				.findFirst()
				.orElse(null);
		if (blueprint == null) return ShipUpgradeResponse.BLUEPRINT_NOT_FOUND;
		if (ship.getLevel() >= blueprint.getLevels().size()) return ShipUpgradeResponse.MAX_LEVEL_REACHED;
		ResourceData playerResources = API.meta().resources(token);
		for (ResourceAmount resourceCost : blueprint.getLevels().get(ship.getLevel()).getCost()) {
			if (!playerResources.contains(resourceCost)) {
				return ShipUpgradeResponse.NOT_ENOUGH_RESOURCES;
			}
		}
		ResourceData resourceUpdate = new ResourceData();
		resourceUpdate.setResources(blueprint.getLevels().get(ship.getLevel()).getCost());
		API.meta().addResources(token, resourceUpdate);
		ship.setLevel(ship.getLevel() + 1);
		return ShipUpgradeResponse.SUCCESS;
	}

	@Override
	public ShipAddResponse addBlueprint(String token, int shipid) {
		PlayerShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) return ShipAddResponse.USER_NOT_FOUND;
		if (shipyard.getShips().stream().anyMatch(ship -> ship.getShipId() == shipid)) return ShipAddResponse.ALREADY_OWNED;
		ShipBlueprint blueprint = getBaseData().getShipBlueprints().stream()
				.filter(bp -> bp.getId() == shipid)
				.findFirst()
				.orElse(null);
		if (blueprint == null) return ShipAddResponse.SHIP_NOT_FOUND;
		if (shipyard.getShips().size() >= shipyard.getSlots()) return ShipAddResponse.HANGAR_FULL;
		PlayerShipData newShip = new PlayerShipData();
		newShip.setUuid(UUID.randomUUID().toString());
		newShip.setShipId(shipid);
		newShip.setBuilt(false);
		newShip.setInUse(false);
		newShip.setLevel(0);
		shipyard.getShips().add(newShip);
		return ShipAddResponse.SUCCESS;
	}
	
}
