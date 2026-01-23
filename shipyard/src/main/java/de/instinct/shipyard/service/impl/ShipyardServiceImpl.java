package de.instinct.shipyard.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.api.core.API;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.meta.dto.Resource;
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
import de.instinct.api.shipyard.dto.admin.ShipCreateRequest;
import de.instinct.api.shipyard.dto.admin.ShipCreateResponse;
import de.instinct.api.shipyard.dto.admin.buildcost.BuildCostCreateRequest;
import de.instinct.api.shipyard.dto.admin.buildcost.BuildCostCreateResponse;
import de.instinct.api.shipyard.dto.admin.buildcost.BuildCostDeleteRequest;
import de.instinct.api.shipyard.dto.admin.buildcost.BuildCostDeleteResponse;
import de.instinct.api.shipyard.dto.admin.buildcost.BuildCostUpdateRequest;
import de.instinct.api.shipyard.dto.admin.buildcost.BuildCostUpdateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentCreateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentCreateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentDeleteRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentDeleteResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelCreateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelCreateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelDeleteRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelDeleteResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelUpdateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentLevelUpdateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentUpdateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentUpdateResponse;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeCreateRequest;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeCreateResponse;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeDeleteRequest;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeDeleteResponse;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeUpdateRequest;
import de.instinct.api.shipyard.dto.admin.component.LevelAttributeUpdateResponse;
import de.instinct.api.shipyard.dto.ship.PlayerShipComponentLevel;
import de.instinct.api.shipyard.dto.ship.PlayerShipData;
import de.instinct.api.shipyard.dto.ship.ShipBlueprint;
import de.instinct.api.shipyard.dto.ship.ShipComponent;
import de.instinct.api.shipyard.dto.ship.ShipCore;
import de.instinct.api.shipyard.dto.ship.ShipEngine;
import de.instinct.api.shipyard.dto.ship.ShipHull;
import de.instinct.api.shipyard.dto.ship.ShipShield;
import de.instinct.api.shipyard.dto.ship.ShipStatisticReportRequest;
import de.instinct.api.shipyard.dto.ship.ShipStatisticReportResponse;
import de.instinct.api.shipyard.dto.ship.ShipWeapon;
import de.instinct.api.shipyard.dto.ship.component.ComponentAttribute;
import de.instinct.api.shipyard.dto.ship.component.ComponentLevel;
import de.instinct.api.shipyard.dto.ship.component.ShipComponentType;
import de.instinct.api.shipyard.dto.ship.component.level.CoreLevel;
import de.instinct.api.shipyard.dto.ship.component.level.EngineLevel;
import de.instinct.api.shipyard.dto.ship.component.level.HullLevel;
import de.instinct.api.shipyard.dto.ship.component.level.ShieldLevel;
import de.instinct.api.shipyard.dto.ship.component.level.WeaponLevel;
import de.instinct.api.shipyard.service.impl.ShipyardUtility;
import de.instinct.base.file.FileManager;
import de.instinct.engine.model.ship.components.types.CoreType;
import de.instinct.engine.model.ship.components.types.EngineType;
import de.instinct.engine.model.ship.components.types.HullType;
import de.instinct.engine.model.ship.components.types.ShieldType;
import de.instinct.engine.model.ship.components.types.WeaponType;
import de.instinct.engine.stats.model.unit.ShipStatistic;
import de.instinct.engine.stats.model.unit.component.types.CoreStatistic;
import de.instinct.engine.stats.model.unit.component.types.EngineStatistic;
import de.instinct.engine.stats.model.unit.component.types.HullStatistic;
import de.instinct.engine.stats.model.unit.component.types.ShieldStatistic;
import de.instinct.engine.stats.model.unit.component.types.WeaponStatistic;
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
		PlayerShipyardData initShipyardData = new PlayerShipyardData();
		initShipyardData.setSlots(getBaseData().getBaseSlots());
		initShipyardData.setActiveShipSlots(getBaseData().getBaseActiveShipSlots());
		initShipyardData.setShips(initShips);
		addShip(initShipyardData, "hawk");
		initShipyardData.getShips().get(0).setInUse(true);
		userShipyards.put(token, initShipyardData);
		return ShipyardInitializationResponseCode.SUCCESS;
	}
	
	public void addShip(PlayerShipyardData playerShipyard, String shipname) {
		ShipBlueprint blueprint = getBlueprint(shipname);
		if (blueprint == null) return;
		
		PlayerShipData initShipData = new PlayerShipData();
		initShipData.setUuid(UUID.randomUUID().toString());
		initShipData.setShipId(blueprint.getId());
		initShipData.setBuilt(true);
		initShipData.setComponentLevels(new ArrayList<>());
		for (ShipComponent component : blueprint.getComponents()) {
			PlayerShipComponentLevel playerComponentLevel = new PlayerShipComponentLevel();
			playerComponentLevel.setComponentId(component.getId());
			initShipData.getComponentLevels().add(playerComponentLevel);
		}
		
		playerShipyard.getShips().add(initShipData);
	}
	
	@Override
	public PlayerShipyardData getShipyardData(String token) {
		PlayerShipyardData shipyard = userShipyards.get(token);
		if (shipyard == null) {
			init(token);
			shipyard = userShipyards.get(token);
		}
		validate(shipyard);
		return shipyard;
	}
	
	private void validate(PlayerShipyardData shipyard) {
		for (PlayerShipData playerShip : shipyard.getShips()) {
			ShipBlueprint blueprint = getBlueprint(playerShip.getShipId());
			if (playerShip.getComponentLevels().size() < blueprint.getComponents().size()) {
				for (ShipComponent component : blueprint.getComponents()) {
					boolean hasPlayerLevel = false;
					for (PlayerShipComponentLevel existingLevel : playerShip.getComponentLevels()) {
						if (existingLevel.getComponentId() == component.getId()) {
							hasPlayerLevel = true;
						}
					}
					if (!hasPlayerLevel) {
						PlayerShipComponentLevel playerComponentLevel = new PlayerShipComponentLevel();
						playerComponentLevel.setComponentId(component.getId());
						playerShip.getComponentLevels().add(playerComponentLevel);
					}
				}
			}
		}
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
	
	private ShipBlueprint getBlueprint(String model) {
		for (ShipBlueprint blueprint : shipyardData.getShipBlueprints()) {
			if (blueprint.getModel().equalsIgnoreCase(model)) {
				return blueprint;
			}
		}
		return null;
	}

	private ShipBlueprint getBlueprint(int id) {
		for (ShipBlueprint blueprint : shipyardData.getShipBlueprints()) {
			if (blueprint.getId() == id) {
				return blueprint;
			}
		}
		return null;
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
	public ShipStatisticReportResponse statistic(ShipStatisticReportRequest request) {
		PlayerShipyardData shipyard = userShipyards.get(request.getUserUUID());
		if (shipyard == null) return ShipStatisticReportResponse.USER_NOT_FOUND;
		for (ShipStatistic shipStatistic : request.getShipStatistics()) {
			for (PlayerShipData playerShip : shipyard.getShips()) {
				ShipBlueprint blueprint = getBaseData().getShipBlueprints().stream()
						.filter(bp -> bp.getId() == playerShip.getShipId())
						.findFirst()
						.orElse(null);
				if (blueprint.getModel().contentEquals(shipStatistic.getModel())) {
					applyStatistic(shipStatistic, blueprint, playerShip);
				}
			}
		}
		return ShipStatisticReportResponse.SUCCESS;
	}

	private void applyStatistic(ShipStatistic shipStatistic, ShipBlueprint blueprint, PlayerShipData playerShip) {
		for (ShipComponent component : blueprint.getComponents()) {
			PlayerShipComponentLevel playerComponentLevel = playerShip.getComponentLevels().stream()
                    .filter(cl -> cl.getComponentId() == component.getId())
                    .findFirst()
                    .orElse(null);
			
			float requirementValue = -1;
			for (ComponentLevel level : component.getLevels()) {
				if (level.getLevel() == playerComponentLevel.getLevel()) {
					requirementValue = component.getLevels().get(component.getLevels().indexOf(level) + 1).getRequirementValue();
					updatePlayerComponentLevelProgress(shipStatistic, component, level, playerComponentLevel);
				}
			}
			
			if (requirementValue > -1 && playerComponentLevel.getProgress() >= requirementValue) {
				playerComponentLevel.setLevel(playerComponentLevel.getLevel() + 1);
				playerComponentLevel.setProgress(0);
			}
		}
	}

	private void updatePlayerComponentLevelProgress(ShipStatistic shipStatistic, ShipComponent component, ComponentLevel level, PlayerShipComponentLevel playerComponentLevel) {
		if (component instanceof ShipCore) {
			CoreLevel coreLevel = (CoreLevel) level;
			CoreStatistic coreStatistic = shipStatistic.getCoreStatistic();
			switch (coreLevel.getRequirementType()) {
				case CP_USED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + coreStatistic.getCpUsed());
					break;
				case RESOURCES_USED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + coreStatistic.getResourcesUsed());
					break;
				case TIMES_DEPLOYED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + coreStatistic.getTimesBuilt());
					break;
				case TIMES_DESTROYED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + coreStatistic.getTimesDestroyed());
					break;
			}
		}
		
		if (component instanceof ShipEngine) {
			EngineLevel engineLevel = (EngineLevel) level;
			EngineStatistic engineStatistic = shipStatistic.getEngineStatistic();
			switch (engineLevel.getRequirementType()) {
				case DISTANCE_TRAVELED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + engineStatistic.getDistanceTraveled());
					break;
			}
		}
		
		if (component instanceof ShipHull) {
			HullLevel hullLevel = (HullLevel) level;
			HullStatistic hullStatistic = shipStatistic.getHullStatistic();
			switch (hullLevel.getRequirementType()) {
				case DAMAGE_TAKEN:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + hullStatistic.getDamageTaken());
					break;
				case HULL_REPAIRED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + hullStatistic.getHullRepaired());
					break;
			}
		}
		
		if (component instanceof ShipShield) {
			ShieldLevel shieldLevel = (ShieldLevel) level;
			ShieldStatistic shieldStatistic = getShieldStatistic(shipStatistic, component.getId());
			switch (shieldLevel.getRequirementType()) {
				case DAMAGE_ABSORBED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + shieldStatistic.getDamageAbsorped());
					break;
				case SHIELDS_RECHARGED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + shieldStatistic.getShieldRecharged());
					break;
				case DAMAGE_INSTANCES_BLOCKED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + shieldStatistic.getDamageInstancesBlocked());
					break;
			}
		}
		
		if (component instanceof ShipWeapon) {
			WeaponLevel weaponLevel = (WeaponLevel) level;
			WeaponStatistic weaponStatistic = getWeaponStatistic(shipStatistic, component.getId());
			System.out.println(weaponStatistic);
			switch (weaponLevel.getRequirementType()) {
				case DAMAGE_DEALT:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + weaponStatistic.getDamageDealt());
					break;
				case KILLS:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + weaponStatistic.getKills());
					break;
				case SHOTS_FIRED:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + weaponStatistic.getShotsFired());
					break;
				case COOLED_DOWN_SEC:
					playerComponentLevel.setProgress(playerComponentLevel.getProgress() + weaponStatistic.getCooledDownSec());
					break;
			}
		}
	}

	private ShieldStatistic getShieldStatistic(ShipStatistic shipStatistic, int id) {
		for (ShieldStatistic shieldStatistic : shipStatistic.getShieldStatistics()) {
			if (shieldStatistic.getId() == id) {
				return shieldStatistic;
			}
		}
		return null;
	}
	
	private WeaponStatistic getWeaponStatistic(ShipStatistic shipStatistic, int id) {
		for (WeaponStatistic weaponStatistic : shipStatistic.getWeaponStatistics()) {
			if (weaponStatistic.getId() == id) {
				return weaponStatistic;
			}
		}
		return null;
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
		componentRequest.setShipname(request.getName());
		componentRequest.setType(ShipComponentType.CORE);
		componentRequest.setComponentType(request.getType().toString());
		createComponent(componentRequest);
		
		newBlueprint.setBuildCost(new ArrayList<>());
		shipyardData.getShipBlueprints().add(newBlueprint);
		saveBaseData();
		return ShipCreateResponse.SUCCESS;
	}
	
	@Override
	public BuildCostCreateResponse createBuildCost(BuildCostCreateRequest request) {
		if (request.getShipname() == null) return BuildCostCreateResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return BuildCostCreateResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return BuildCostCreateResponse.SHIP_NOT_FOUND;
		
		ResourceAmount newBuildCost = new ResourceAmount();
		newBuildCost.setId(blueprint.getBuildCost().isEmpty() ? 0 : blueprint.getBuildCost().stream().mapToInt(ResourceAmount::getId).max().getAsInt() + 1);
		newBuildCost.setType(Resource.METAL);
		newBuildCost.setAmount(0);
		blueprint.getBuildCost().add(newBuildCost);
		saveBaseData();
		return BuildCostCreateResponse.SUCCESS;
	}

	@Override
	public BuildCostUpdateResponse updateBuildCost(BuildCostUpdateRequest request) {
		if (request.getShipname() == null) return BuildCostUpdateResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return BuildCostUpdateResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return BuildCostUpdateResponse.SHIP_NOT_FOUND;
		
		boolean updated = false;
		for (ResourceAmount buildCost : blueprint.getBuildCost()) {
			if (buildCost.getId() == request.getId()) {
				buildCost.setType(Resource.valueOf(request.getResourceType().toUpperCase()));
				buildCost.setAmount(request.getCost());
			}
		}
		if (!updated) return BuildCostUpdateResponse.BUILD_COST_NOT_FOUND; 
		
		saveBaseData();
		return BuildCostUpdateResponse.SUCCESS;
	}

	@Override
	public BuildCostDeleteResponse deleteBuildCost(BuildCostDeleteRequest request) {
		if (request.getShipname() == null) return BuildCostDeleteResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return BuildCostDeleteResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return BuildCostDeleteResponse.SHIP_NOT_FOUND;
		
		if (!blueprint.getBuildCost().removeIf(bc -> bc.getId() == request.getId())) return BuildCostDeleteResponse.BUILD_COST_NOT_FOUND;
		
		saveBaseData();
		return BuildCostDeleteResponse.SUCCESS;
	}
	
	@Override
	public ComponentCreateResponse createComponent(ComponentCreateRequest request) {
		if (request.getShipname() == null) return ComponentCreateResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return ComponentCreateResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
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
		if (request.getShipname() == null) return ComponentDeleteResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return ComponentDeleteResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return ComponentDeleteResponse.SHIP_NOT_FOUND;
		
		if (!blueprint.getComponents().removeIf(c -> c.getId() == request.getId())) return ComponentDeleteResponse.COMPONENT_NOT_FOUND;
		
		saveBaseData();
		return ComponentDeleteResponse.SUCCESS;
	}

	@Override
	public ComponentUpdateResponse updateComponent(ComponentUpdateRequest request) {
		if (request.getShipname() == null) return ComponentUpdateResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return ComponentUpdateResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return ComponentUpdateResponse.SHIP_NOT_FOUND;
		
		boolean updated = false;
		for (ShipComponent component : blueprint.getComponents()) {
			if (component.getId() == request.getId()) {
				ShipyardUtility.updateShipComponentType(component, request);
				updated = true;
			}
		}
		if (!updated) return ComponentUpdateResponse.COMPONENT_NOT_FOUND; 
		
		saveBaseData();
		return ComponentUpdateResponse.SUCCESS;
	}

	@Override
	public ComponentLevelCreateResponse createComponentLevel(ComponentLevelCreateRequest request) {
		if (request.getShipname() == null) return ComponentLevelCreateResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return ComponentLevelCreateResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return ComponentLevelCreateResponse.SHIP_NOT_FOUND;
		
		boolean updated = false;
		for (ShipComponent component : blueprint.getComponents()) {
			if (component.getId() == request.getComponentID()) {
				ShipyardUtility.createShipComponentLevel(component);
				updated = true;
			}
		}
		if (!updated) return ComponentLevelCreateResponse.COMPONENT_NOT_FOUND; 
		
		saveBaseData();
		return ComponentLevelCreateResponse.SUCCESS;
	}

	@Override
	public ComponentLevelUpdateResponse updateComponentLevel(ComponentLevelUpdateRequest request) {
		if (request.getShipname() == null) return ComponentLevelUpdateResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return ComponentLevelUpdateResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return ComponentLevelUpdateResponse.SHIP_NOT_FOUND;
		
		boolean foundComponent = false;
		for (ShipComponent component : blueprint.getComponents()) {
			if (component.getId() == request.getComponentId()) {
				boolean foundLevel = false;
				for (ComponentLevel level : component.getLevels()) {
					if (level.getLevel() == request.getLevel()) {
						ShipyardUtility.updateComponentLevel(level, request.getRequirementValue(), request.getRequirementType());
						foundLevel = true;
					}
				}
				if (!foundLevel) return ComponentLevelUpdateResponse.COMPONENT_LEVEL_NOT_FOUND; 
				foundComponent = true;
			}
		}
		if (!foundComponent) return ComponentLevelUpdateResponse.COMPONENT_NOT_FOUND; 
		
		saveBaseData();
		return ComponentLevelUpdateResponse.SUCCESS;
	}

	@Override
	public ComponentLevelDeleteResponse deleteComponentLevel(ComponentLevelDeleteRequest request) {
		if (request.getShipname() == null) return ComponentLevelDeleteResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return ComponentLevelDeleteResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return ComponentLevelDeleteResponse.SHIP_NOT_FOUND;
		
		boolean updated = false;
		for (ShipComponent component : blueprint.getComponents()) {
			if (component.getId() == request.getComponentId()) {
				if (!component.getLevels().removeIf(c -> c.getLevel() == request.getLevel())) return ComponentLevelDeleteResponse.COMPONENT_LEVEL_NOT_FOUND;
				updated = true;
			}
		}
		if (!updated) return ComponentLevelDeleteResponse.COMPONENT_NOT_FOUND; 
		
		saveBaseData();
		return ComponentLevelDeleteResponse.SUCCESS;
	}
	
	@Override
	public LevelAttributeCreateResponse createLevelAttribute(LevelAttributeCreateRequest request) {
		if (request.getShipname() == null) return LevelAttributeCreateResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return LevelAttributeCreateResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return LevelAttributeCreateResponse.SHIP_NOT_FOUND;
		
		boolean foundComponent = false;
		boolean foundLevel = false;
		for (ShipComponent component : blueprint.getComponents()) {
			if (component.getId() == request.getComponentId()) {
				for (ComponentLevel componentLevel : component.getLevels()) {
					if (componentLevel.getLevel() == request.getLevel()) {
						ShipyardUtility.createShipLevelAttribute(componentLevel);
						foundLevel = true;
					}
				}
				foundComponent = true;
			}
		}
		if (!foundLevel) return LevelAttributeCreateResponse.LEVEL_INVALID;
		if (!foundComponent) return LevelAttributeCreateResponse.COMPONENT_NOT_FOUND; 
		
		saveBaseData();
		return LevelAttributeCreateResponse.SUCCESS;
	}

	@Override
	public LevelAttributeUpdateResponse updateLevelAttribute(LevelAttributeUpdateRequest request) {
		if (request.getShipname() == null) return LevelAttributeUpdateResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return LevelAttributeUpdateResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return LevelAttributeUpdateResponse.SHIP_NOT_FOUND;
		
		boolean foundComponent = false;
		boolean foundLevel = false;
		for (ShipComponent component : blueprint.getComponents()) {
			if (component.getId() == request.getComponentId()) {
				for (ComponentLevel componentLevel : component.getLevels()) {
					if (componentLevel.getLevel() == request.getLevel()) {
						for (ComponentAttribute attribute : componentLevel.getAttributes()) {
							if (attribute.getId() == request.getAttributeId()) {
								ShipyardUtility.updateLevelAttribute(attribute, request.getType(), request.getValue());
								foundLevel = true;
							}
						}
						foundLevel = true;
					}
				}
				foundComponent = true;
			}
		}
		if (!foundLevel) return LevelAttributeUpdateResponse.LEVEL_INVALID;
		if (!foundComponent) return LevelAttributeUpdateResponse.COMPONENT_NOT_FOUND; 
		
		saveBaseData();
		return LevelAttributeUpdateResponse.SUCCESS;
	}

	@Override
	public LevelAttributeDeleteResponse deleteLevelAttribute(LevelAttributeDeleteRequest request) {
		if (request.getShipname() == null) return LevelAttributeDeleteResponse.NAME_NULL;
		if (request.getShipname().trim().contentEquals("")) return LevelAttributeDeleteResponse.NAME_EMPTY;
		ShipBlueprint blueprint = null;
		for (ShipBlueprint existingBlueprint : shipyardData.getShipBlueprints()) {
			if (existingBlueprint.getModel().equalsIgnoreCase(request.getShipname())) {
				blueprint = existingBlueprint;
			}
		}
		if (blueprint == null) return LevelAttributeDeleteResponse.SHIP_NOT_FOUND;
		
		boolean foundComponent = false;
		boolean foundLevel = false;
		for (ShipComponent component : blueprint.getComponents()) {
			if (component.getId() == request.getComponentId()) {
				for (ComponentLevel componentLevel : component.getLevels()) {
					if (componentLevel.getLevel() == request.getLevel()) {
						if (!componentLevel.getAttributes().removeIf(c -> c.getId() == request.getAttributeId())) return LevelAttributeDeleteResponse.ATTRIBUTE_NOT_FOUND;
						foundLevel = true;
					}
				}
				foundComponent = true;
			}
		}
		if (!foundLevel) return LevelAttributeDeleteResponse.LEVEL_INVALID;
		if (!foundComponent) return LevelAttributeDeleteResponse.COMPONENT_NOT_FOUND; 
		
		saveBaseData();
		return LevelAttributeDeleteResponse.SUCCESS;
	}
	
}
