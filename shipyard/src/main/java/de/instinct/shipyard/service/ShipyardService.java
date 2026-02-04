package de.instinct.shipyard.service;

import de.instinct.api.matchmaking.dto.PlayerShipResult;
import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shipyard.dto.ShipAddResponse;
import de.instinct.api.shipyard.dto.ShipBuildResponse;
import de.instinct.api.shipyard.dto.ShipUpgradeResponse;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.ShipyardInitializationResponseCode;
import de.instinct.api.shipyard.dto.StatChangeResponse;
import de.instinct.api.shipyard.dto.UnuseShipResponseCode;
import de.instinct.api.shipyard.dto.UseShipResponseCode;
import de.instinct.api.shipyard.dto.admin.DeleteShipResponse;
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
import de.instinct.engine_api.ship.model.ShipStatisticReportRequest;

public interface ShipyardService {

	ShipyardInitializationResponseCode init(String token);
	
	PlayerShipyardData getShipyardData(String token);
	
	ShipyardData getBaseData();
	
	void loadBaseData();

	UseShipResponseCode useShip(String token, String shipUUID);
	
	UnuseShipResponseCode unuseShip(String token, String shipUUID);
	
	StatChangeResponse changeHangarSpace(String token, int count);

	StatChangeResponse changeActiveShips(String token, int count);

	ShipBuildResponse build(String token, String shiptoken);

	ShipUpgradeResponse upgrade(String token, String shiptoken);

	ShipAddResponse addBlueprint(String token, String model);
	
	PlayerShipResult statistic(ShipStatisticReportRequest request);

	ShipCreateResponse createShip(ShipCreateRequest request);
	
	DeleteShipResponse deleteShip(String id);
	
	BuildCostCreateResponse createBuildCost(BuildCostCreateRequest request);
	
	BuildCostUpdateResponse updateBuildCost(BuildCostUpdateRequest request);
	
	BuildCostDeleteResponse deleteBuildCost(BuildCostDeleteRequest request);
	
	ComponentCreateResponse createComponent(ComponentCreateRequest request);

	ComponentDeleteResponse deleteComponent(ComponentDeleteRequest request);

	ComponentUpdateResponse updateComponent(ComponentUpdateRequest request);

	ComponentLevelCreateResponse createComponentLevel(ComponentLevelCreateRequest request);

	ComponentLevelUpdateResponse updateComponentLevel(ComponentLevelUpdateRequest request);

	ComponentLevelDeleteResponse deleteComponentLevel(ComponentLevelDeleteRequest request);
	
	LevelAttributeCreateResponse createLevelAttribute(LevelAttributeCreateRequest request);

	LevelAttributeUpdateResponse updateLevelAttribute(LevelAttributeUpdateRequest request);

	LevelAttributeDeleteResponse deleteLevelAttribute(LevelAttributeDeleteRequest request);

}
