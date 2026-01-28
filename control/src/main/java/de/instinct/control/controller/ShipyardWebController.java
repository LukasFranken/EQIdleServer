package de.instinct.control.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
import de.instinct.control.service.base.BaseService;
import de.instinct.control.service.shipyard.ShipyardControlService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("shipyard")
public class ShipyardWebController {
	
	private final BaseService baseService;
	private final ShipyardControlService shipyardService;
	
	@GetMapping("")
    public String home(Model model) {
		baseService.setModel(model);
		shipyardService.setModel(model);
		model.addAttribute("panel", "shipyard");
        return "home";
    }

    @GetMapping("/refresh")
    public String refresh(Model model) {
        return home(model);
    }
    
    @PostMapping("/create/ship")
    public ResponseEntity<ShipCreateResponse> createShip(@RequestBody ShipCreateRequest request) {
    	return ResponseEntity.ok(shipyardService.createShip(request));
    }
    
    @PostMapping("/create/buildcost")
    public ResponseEntity<BuildCostCreateResponse> createBuildCost(@RequestBody BuildCostCreateRequest request) {
    	return ResponseEntity.ok(shipyardService.createBuildCost(request));
    }
    
    @PostMapping("/update/buildcost")
    public ResponseEntity<BuildCostUpdateResponse> updateBuildCost(@RequestBody BuildCostUpdateRequest request) {
    	return ResponseEntity.ok(shipyardService.updateBuildCost(request));
    }
    
    @PostMapping("/delete/buildcost")
    public ResponseEntity<BuildCostDeleteResponse> deleteBuildCost(@RequestBody BuildCostDeleteRequest request) {
    	return ResponseEntity.ok(shipyardService.deleteBuildCost(request));
    }
    
    @PostMapping("/create/component")
    public ResponseEntity<ComponentCreateResponse> createComponent(@RequestBody ComponentCreateRequest request) {
    	return ResponseEntity.ok(shipyardService.createComponent(request));
    }
    
    @PostMapping("/update/component")
    public ResponseEntity<ComponentUpdateResponse> updateComponent(@RequestBody ComponentUpdateRequest request) {
    	return ResponseEntity.ok(shipyardService.updateComponent(request));
    }
    
    @PostMapping("/delete/component")
    public ResponseEntity<ComponentDeleteResponse> deleteComponent(@RequestBody ComponentDeleteRequest request) {
    	return ResponseEntity.ok(shipyardService.deleteComponent(request));
    }
    
    @PostMapping("/create/componentlevel")
    public ResponseEntity<ComponentLevelCreateResponse> createComponentLevel(@RequestBody ComponentLevelCreateRequest request) {
    	return ResponseEntity.ok(shipyardService.createComponentLevel(request));
    }
    
    @PostMapping("/update/componentlevel")
    public ResponseEntity<ComponentLevelUpdateResponse> updateComponentLevel(@RequestBody ComponentLevelUpdateRequest request) {
    	return ResponseEntity.ok(shipyardService.updateComponentLevel(request));
    }
    
    @PostMapping("/delete/componentlevel")
    public ResponseEntity<ComponentLevelDeleteResponse> deleteComponentLevel(@RequestBody ComponentLevelDeleteRequest request) {
    	return ResponseEntity.ok(shipyardService.deleteComponentLevel(request));
    }
    
    @PostMapping("/create/levelattribute")
    public ResponseEntity<LevelAttributeCreateResponse> createLevelAttribute(@RequestBody LevelAttributeCreateRequest request) {
    	return ResponseEntity.ok(shipyardService.createLevelAttribute(request));
    }
    
    @PostMapping("/update/levelattribute")
    public ResponseEntity<LevelAttributeUpdateResponse> updateLevelAttribute(@RequestBody LevelAttributeUpdateRequest request) {
    	return ResponseEntity.ok(shipyardService.updateLevelAttribute(request));
    }
    
    @PostMapping("/delete/levelattribute")
    public ResponseEntity<LevelAttributeDeleteResponse> deleteLevelAttribute(@RequestBody LevelAttributeDeleteRequest request) {
    	return ResponseEntity.ok(shipyardService.deleteLevelAttribute(request));
    }
    
    @GetMapping("/module/{type}")
    public String selectTypeModule(Model model, @PathVariable("type") String type) {
		shipyardService.prepareShipTable(model, type);
		model.addAttribute("activeShipType", type);
		return home(model);
    }
    
    @GetMapping("/modal/shipoverviewmodal/{shipname}")
    public String getShipOverviewModal(Model model, @PathVariable("shipname") String shipname) {
    	shipyardService.prepareOverviewModal(model, shipname);
        return "content/modal/shipoverview/shipoverviewmodal :: shipoverviewmodal";
    }
    
    @GetMapping("/modal/buildcostmodal/{shipname}")
    public String getBuildCostModal(Model model, @PathVariable("shipname") String shipname) {
    	shipyardService.prepareBuildCostModal(model, shipname);
        return "content/modal/buildcostmodal :: buildcostmodal";
    }
    
    @GetMapping("/modal/shipoverviewmodal/fragment/{shipname}")
    public String getComponents(Model model, @PathVariable("shipname") String shipname) {
    	shipyardService.prepareOverviewModal(model, shipname);
        return "content/modal/shipoverview/fragments/components :: components";
    }
    
    @GetMapping("/modal/shipoverviewmodal/fragment/{shipname}/{componentID}")
    public String getComponentLevels(Model model, @PathVariable("shipname") String shipname, @PathVariable("componentID") int componentID) {
        shipyardService.prepareComponentLevelTable(model, shipname, componentID);
        return "content/modal/shipoverview/fragments/componentlevels :: componentlevels";
    }
    
    @GetMapping("/modal/shipoverviewmodal/fragment/{shipname}/{componentID}/{level}")
    public String getLevelAttibutes(Model model, @PathVariable("shipname") String shipname, @PathVariable("componentID") int componentID, @PathVariable("level") int level) {
        shipyardService.prepareLevelAttributeTable(model, shipname, componentID, level);
        return "content/modal/shipoverview/fragments/levelattributes :: levelattributes";
    }
    
    @GetMapping("/resource-types")
    public ResponseEntity<List<String>> getResourceTypes() {
        return ResponseEntity.ok(shipyardService.getResourceTypes());
    }
    
    @GetMapping("/component-types/{type}")
    public ResponseEntity<List<String>> getComponentTypes(@PathVariable("type") String type) {
        return ResponseEntity.ok(shipyardService.getComponentTypes(type));
    }
    
    @GetMapping("/component-level-types/{type}")
    public ResponseEntity<List<String>> getComponentLevelTypes(@PathVariable("type") String type) {
        return ResponseEntity.ok(shipyardService.getComponentLevelTypes(type));
    }
    
    @GetMapping("/level-attribute-types/{type}")
    public ResponseEntity<List<String>> getLevelAttributeTypes(@PathVariable("type") String type) {
        return ResponseEntity.ok(shipyardService.getLevelAttributeTypes(type));
    }

}