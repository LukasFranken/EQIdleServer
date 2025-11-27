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
import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shipyard.dto.ShipAddResponse;
import de.instinct.api.shipyard.dto.ShipBuildResponse;
import de.instinct.api.shipyard.dto.ShipUpgradeResponse;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.StatChangeResponse;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;
import de.instinct.api.shipyard.dto.admin.ComponentCreateRequest;
import de.instinct.api.shipyard.dto.admin.ComponentCreateResponse;
import de.instinct.api.shipyard.dto.admin.ComponentDeleteRequest;
import de.instinct.api.shipyard.dto.admin.ComponentDeleteResponse;
import de.instinct.api.shipyard.dto.admin.ShipCreateRequest;
import de.instinct.api.shipyard.dto.admin.ShipCreateResponse;
import de.instinct.api.shipyard.dto.ship.PlayerShipData;
import de.instinct.api.shipyard.dto.ship.ShipBlueprint;
import de.instinct.api.shipyard.dto.ship.ShipComponent;
import de.instinct.api.shipyard.dto.ship.ShipCore;
import de.instinct.api.shipyard.dto.ship.ShipEngine;
import de.instinct.api.shipyard.dto.ship.ShipHull;
import de.instinct.api.shipyard.dto.ship.ShipShield;
import de.instinct.api.shipyard.dto.ship.ShipWeapon;
import de.instinct.api.shipyard.dto.ship.component.ShipComponentType;
import de.instinct.base.file.FileManager;
import de.instinct.engine.model.ship.components.types.CoreType;
import de.instinct.engine.model.ship.components.types.EngineType;
import de.instinct.engine.model.ship.components.types.HullType;
import de.instinct.engine.model.ship.components.types.ShieldType;
import de.instinct.engine.model.ship.components.types.WeaponType;
import de.instinct.shipyard.service.ShipyardService;
import de.instinct.shipyard.service.model.ShipyardBaseData;

@Service
public class ShipyardServiceImpl implements ShipyardService {
	
	private ShipyardData shipyardData;
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
		initShipData.setComponentLevels(new ArrayList<>());
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
		if (shipyardData == null) {
			loadBaseData();
		}
		return shipyardData;
	}
	
	@Override
	public void loadBaseData() {
		shipyardData = new ShipyardData();
		ShipyardBaseData baseData = ObjectJSONMapper.mapJSON(FileManager.loadFile("base.data"), ShipyardBaseData.class);
		shipyardData.setBaseActiveShipSlots(baseData.getBaseActiveShipSlots());
		shipyardData.setBaseSlots(baseData.getBaseSlots());
		shipyardData.setCurrentShipId(baseData.getCurrentShipId());
		shipyardData.setShipBlueprints(new ArrayList<>());
		for (String blueprintTag : baseData.getBlueprintTags()) {
			ShipBlueprint shipBlueprint = ObjectJSONMapper.mapJSON(FileManager.loadFile("blueprints/" + blueprintTag + ".data"), ShipBlueprint.class);
			shipyardData.getShipBlueprints().add(shipBlueprint);
		}
	}
	
	public void saveBaseData() {
		ShipyardBaseData baseData = new ShipyardBaseData();
		baseData.setBaseActiveShipSlots(shipyardData.getBaseActiveShipSlots());
		baseData.setBaseSlots(shipyardData.getBaseSlots());
		baseData.setCurrentShipId(shipyardData.getCurrentShipId());
		List<String> blueprintTags = new ArrayList<>();
		for (ShipBlueprint blueprint : shipyardData.getShipBlueprints()) {
			blueprintTags.add(blueprint.getModel().toLowerCase());
			FileManager.saveFile("blueprints/" + blueprint.getModel().toLowerCase() + ".data", ObjectJSONMapper.mapObject(blueprint));
		}
		baseData.setBlueprintTags(blueprintTags);
		FileManager.saveFile("base.data", ObjectJSONMapper.mapObject(baseData));
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
		if (shipyard.getUsedSlots() >= shipyard.getSlots()) return ShipBuildResponse.HANGAR_FULL;
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
		/*PlayerShipyardData shipyard = userShipyards.get(token);
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
		ship.setLevel(ship.getLevel() + 1);*/
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
		PlayerShipData newShip = new PlayerShipData();
		newShip.setUuid(UUID.randomUUID().toString());
		newShip.setShipId(shipid);
		newShip.setBuilt(false);
		newShip.setInUse(false);
		shipyard.getShips().add(newShip);
		return ShipAddResponse.SUCCESS;
	}

	@Override
	public ShipCreateResponse createShip(ShipCreateRequest request) {
		if (request.getName() == null) return ShipCreateResponse.NAME_NULL;
		if (request.getName().trim().contentEquals("")) return ShipCreateResponse.NAME_EMPTY;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getName())) {
				return ShipCreateResponse.NAME_TAKEN;
			}
		}
		ShipBlueprint newBlueprint = new ShipBlueprint();
		newBlueprint.setId(shipyardData.getCurrentShipId());
		shipyardData.setCurrentShipId(shipyardData.getCurrentShipId() + 1);
		newBlueprint.setModel(request.getName());
		newBlueprint.setCreated(System.currentTimeMillis());
		newBlueprint.setLastModified(System.currentTimeMillis());
		
		newBlueprint.setComponents(new ArrayList<>());
		ComponentCreateRequest componentRequest = new ComponentCreateRequest();
		componentRequest.setName(request.getName());
		componentRequest.setType(ShipComponentType.CORE);
		componentRequest.setComponentType(request.getType().toString());
		createComponent(componentRequest);
		
		newBlueprint.setBuildCost(new ArrayList<>());
		shipyardData.getShipBlueprints().add(newBlueprint);
		saveBaseData();
		return ShipCreateResponse.SUCCESS;
	}
	
	@Override
	public ComponentCreateResponse createComponent(ComponentCreateRequest request) {
		if (request.getName() == null) return ComponentCreateResponse.NAME_NULL;
		if (request.getName().trim().contentEquals("")) return ComponentCreateResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getName())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return ComponentCreateResponse.SHIP_NOT_FOUND;
		ShipComponent component = null;
		switch (request.getType()) {
			case CORE:
				component = new ShipCore();
				((ShipCore)component).setType(CoreType.valueOf(request.getComponentType()));
				break;
			case WEAPON:
				component = new ShipWeapon();
				((ShipWeapon)component).setType(WeaponType.valueOf(request.getComponentType()));
				break;
			case SHIELD:
				component = new ShipShield();
				((ShipShield)component).setType(ShieldType.valueOf(request.getComponentType()));
				break;
			case ENGINE:
				component = new ShipEngine();
				((ShipEngine)component).setType(EngineType.valueOf(request.getComponentType()));
				break;
			case HULL:
				component = new ShipHull();
				((ShipHull)component).setType(HullType.valueOf(request.getComponentType()));
				break;
		}
		component.setId(blueprint.getComponents().isEmpty() ? 0 : blueprint.getComponents().stream().mapToInt(ShipComponent::getId).max().getAsInt() + 1);
		component.setLevels(new ArrayList<>());
		blueprint.getComponents().add(component);
		saveBaseData();
		return ComponentCreateResponse.SUCCESS;
	}

	@Override
	public ComponentDeleteResponse deleteComponent(ComponentDeleteRequest request) {
		if (request.getName() == null) return ComponentDeleteResponse.NAME_NULL;
		if (request.getName().trim().contentEquals("")) return ComponentDeleteResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getName())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return ComponentDeleteResponse.SHIP_NOT_FOUND;
		
		blueprint.getComponents().removeIf(c -> c.getId() == request.getId());
		
		saveBaseData();
		return ComponentDeleteResponse.SUCCESS;
	}
	
}
