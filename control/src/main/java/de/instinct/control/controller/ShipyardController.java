package de.instinct.control.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import de.instinct.control.component.table.Table;
import de.instinct.control.component.table.TableCell;
import de.instinct.control.component.table.TableHeader;
import de.instinct.control.component.table.TableRow;
import de.instinct.control.service.base.BaseService;
import de.instinct.control.service.shipyard.ShipyardService;
import de.instinct.control.service.shipyard.model.AttributeItem;
import de.instinct.control.service.shipyard.model.ComponentLevelItem;
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
		List<TableHeader> headers = new ArrayList<>();
		headers.add(TableHeader.builder()
				.label("ID")
				.className("id-column")
				.build());
		headers.add(TableHeader.builder()
				.label("Name")
				.className("")
				.build());
		List<TableRow> rows = new ArrayList<>();
		/*rows.add(TableRow.builder()
				.cells(List.of(TableCell.builder().value("0").attributes(new HashMap<>()).build(), TableCell.builder().value("hawk").attributes(new HashMap<>()).build()))
				.build());*/
		rows.add(TableRow.builder()
				.cells(List.of(TableCell.builder().value("1").build(), TableCell.builder().value("turtle").attributes("param=turtle").build()))
				.build());
		/*rows.add(TableRow.builder()
				.cells(List.of(TableCell.builder().value("2").attributes(new HashMap<>()).build(), TableCell.builder().value("shark").attributes(new HashMap<>()).build()))
				.build());*/
    	model.addAttribute("shiptable", Table.builder()
    			.headers(headers)
    			.rows(rows)
				.build());
        return "home";
    }
	
	@GetMapping("/module/{module}")
    public String shipyardpage(Model model, @PathVariable String module) {
		shipyardService.prepareShipTable(model, module);
		return home(model);
    }
	
    @PostMapping("/create")
    public ResponseEntity<ShipCreateResponse> createShip(@RequestBody ShipCreateRequest request) {
    	return ResponseEntity.ok(shipyardService.createShip(request));
    }

    @GetMapping("/refresh")
    public String refresh(Model model) {
        return home(model);
    }
    
    @GetMapping("/modal/{modalName}/{shipname}")
    public String getModal(@PathVariable String modalName, @PathVariable String shipname, Model model) {
    	shipyardService.prepareOverviewModal(shipname, model);
    	List<String> components = new ArrayList<>();
    	components.add("core");
    	components.add("engine");
    	components.add("hull");
    	components.add("shield");
    	components.add("weapon");
    	model.addAttribute("components", components);
    	List<ComponentLevelItem> componentlevels = new ArrayList<>();
    	componentlevels.add(ComponentLevelItem.builder()
    			.level(0)
    			.requirementType("CP_USED")
    			.requirementValue(0)
    			.build());
    	componentlevels.add(ComponentLevelItem.builder()
    			.level(1)
    			.requirementType("CP_USED")
    			.requirementValue(100)
    			.build());
    	componentlevels.add(ComponentLevelItem.builder()
    			.level(2)
    			.requirementType("CP_USED")
    			.requirementValue(1000)
    			.build());
    	componentlevels.add(ComponentLevelItem.builder()
    			.level(3)
    			.requirementType("CP_USED")
    			.requirementValue(1000)
    			.build());
    	componentlevels.add(ComponentLevelItem.builder()
    			.level(4)
    			.requirementType("CP_USED")
    			.requirementValue(1000)
    			.build());
    	List<AttributeItem> attributes = new ArrayList<>();
    	attributes.add(AttributeItem.builder()
				.name("CP_COST")
				.value(1)
				.build());
    	attributes.add(AttributeItem.builder()
				.name("CP_COST")
				.value(1)
				.build());
    	attributes.add(AttributeItem.builder()
				.name("CP_COST")
				.value(1)
				.build());
    	attributes.add(AttributeItem.builder()
				.name("CP_COST")
				.value(1)
				.build());
    	attributes.add(AttributeItem.builder()
				.name("RESOURCE_COST")
				.value(4)
				.build());
    	model.addAttribute("attributes", attributes);
    	model.addAttribute("componentlevels", componentlevels);
        return "content/modal/" + modalName + " :: " + modalName;
    }
	
}