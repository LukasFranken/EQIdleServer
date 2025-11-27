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

import de.instinct.api.shipyard.dto.admin.ComponentCreateRequest;
import de.instinct.api.shipyard.dto.admin.ComponentCreateResponse;
import de.instinct.api.shipyard.dto.admin.ComponentDeleteRequest;
import de.instinct.api.shipyard.dto.admin.ComponentDeleteResponse;
import de.instinct.api.shipyard.dto.admin.ShipCreateRequest;
import de.instinct.api.shipyard.dto.admin.ShipCreateResponse;
import de.instinct.control.service.base.BaseService;
import de.instinct.control.service.shipyard.ShipyardService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("shipyard")
public class ShipyardController {
	
	private final BaseService baseService;
	private final ShipyardService shipyardService;
	
	@GetMapping("")
    public String home(Model model) {
		baseService.setModel(model);
		shipyardService.setModel(model);
		model.addAttribute("panel", "shipyard");
		model.addAttribute("modal", "basemodal");
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
    
    @PostMapping("/create/component")
    public ResponseEntity<ComponentCreateResponse> createComponent(@RequestBody ComponentCreateRequest request) {
    	return ResponseEntity.ok(shipyardService.createComponent(request));
    }
    
    @PostMapping("/delete/component")
    public ResponseEntity<ComponentDeleteResponse> deleteComponent(@RequestBody ComponentDeleteRequest request) {
    	return ResponseEntity.ok(shipyardService.deleteComponent(request));
    }
    
    @GetMapping("/module/{type}")
    public String selectTypeModule(Model model, @PathVariable("type") String type) {
		shipyardService.prepareShipTable(model, type);
		return home(model);
    }
    
    @GetMapping("/modal/shipoverviewmodal/{shipname}")
    public String getModal(Model model, @PathVariable("shipname") String shipname) {
    	shipyardService.prepareOverviewModal(model, shipname);
        return "content/modal/shipoverviewmodal :: shipoverviewmodal";
    }
    
    @GetMapping("/modal/shipoverviewmodal/{shipname}/{componentID}")
    public String getComponentLevels(Model model, @PathVariable("shipname") String shipname, @PathVariable("componentID") int componentID) {
        shipyardService.prepareComponentLevelTable(model, shipname, componentID);
        return "content/modal/shipoverviewmodalfragments/componentlevelstable :: componentlevelstable";
    }
    
    @GetMapping("/modal/shipoverviewmodal/{shipname}/{componentID}/{level}")
    public String getComponentLevels(Model model, @PathVariable("shipname") String shipname, @PathVariable("componentID") int componentID, @PathVariable("level") int level) {
        shipyardService.prepareLevelAttributeTable(model, shipname, componentID, level);
        return "content/modal/shipoverviewmodalfragments/levelattributetable :: levelattributetable";
    }
    
    @GetMapping("/component-types/{type}")
    public ResponseEntity<List<String>> getComponentTypes(@PathVariable("type") String type) {
        return ResponseEntity.ok(shipyardService.getComponentTypes(type));
    }

	
}