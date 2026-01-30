package de.instinct.control.service.shipyard;

import java.util.List;

import org.springframework.ui.Model;

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
import de.instinct.control.service.ModelService;

public interface ShipyardControlService extends ModelService {

	ShipCreateResponse createShip(ShipCreateRequest request);
	
	DeleteShipResponse deleteShip(String id);
	
	BuildCostCreateResponse createBuildCost(BuildCostCreateRequest request);
	
	BuildCostUpdateResponse updateBuildCost(BuildCostUpdateRequest request);
	
	BuildCostDeleteResponse deleteBuildCost(BuildCostDeleteRequest request);
	
	ComponentCreateResponse createComponent(ComponentCreateRequest request);
	
	ComponentUpdateResponse updateComponent(ComponentUpdateRequest request);
	
	ComponentDeleteResponse deleteComponent(ComponentDeleteRequest request);
	
	ComponentLevelCreateResponse createComponentLevel(ComponentLevelCreateRequest request);
	
	ComponentLevelUpdateResponse updateComponentLevel(ComponentLevelUpdateRequest request);
	
	ComponentLevelDeleteResponse deleteComponentLevel(ComponentLevelDeleteRequest request);
	
	LevelAttributeCreateResponse createLevelAttribute(LevelAttributeCreateRequest request);

	LevelAttributeUpdateResponse updateLevelAttribute(LevelAttributeUpdateRequest request);

	LevelAttributeDeleteResponse deleteLevelAttribute(LevelAttributeDeleteRequest request);
	
	void prepareShipTable(Model model, String type);
	
	void prepareBuildCostModal(Model model, String shipname);

	void prepareOverviewModal(Model model, String shipname);

	void prepareComponentLevelTable(Model model, String shipname, int componentID);

	void prepareLevelAttributeTable(Model model, String shipname, int componentID, int level);
	
	List<String> getResourceTypes();

	List<String> getComponentTypes(String type);

	List<String> getComponentLevelTypes(String type);

	List<String> getLevelAttributeTypes(String type);

}
