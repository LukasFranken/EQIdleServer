package de.instinct.control.service.shipyard.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import de.instinct.api.core.API;
import de.instinct.api.shipyard.dto.ShipyardData;
import de.instinct.api.shipyard.dto.admin.ShipCreateRequest;
import de.instinct.api.shipyard.dto.admin.ShipCreateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentCreateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentCreateResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentDeleteRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentDeleteResponse;
import de.instinct.api.shipyard.dto.admin.component.ComponentUpdateRequest;
import de.instinct.api.shipyard.dto.admin.component.ComponentUpdateResponse;
import de.instinct.api.shipyard.dto.ship.ShipBlueprint;
import de.instinct.api.shipyard.dto.ship.ShipComponent;
import de.instinct.api.shipyard.dto.ship.ShipCore;
import de.instinct.api.shipyard.dto.ship.component.ComponentAttribute;
import de.instinct.api.shipyard.dto.ship.component.ComponentLevel;
import de.instinct.api.shipyard.dto.ship.component.ShipComponentType;
import de.instinct.api.shipyard.service.impl.ShipyardUtility;
import de.instinct.control.component.table.Table;
import de.instinct.control.component.table.TableCell;
import de.instinct.control.component.table.TableHeader;
import de.instinct.control.component.table.TableRow;
import de.instinct.control.service.base.model.Link;
import de.instinct.control.service.shipyard.ShipyardService;
import de.instinct.engine.model.ship.components.types.CoreType;
import de.instinct.engine.model.ship.components.types.EngineType;
import de.instinct.engine.model.ship.components.types.HullType;
import de.instinct.engine.model.ship.components.types.ShieldType;
import de.instinct.engine.model.ship.components.types.WeaponType;
import de.instinct.eqspringutils.StringUtils;

@Service
public class ShipyardServiceImpl implements ShipyardService {
	
	private ShipyardData shipyardData;

	@Override
	public void setModel(Model model) {
		List<Link> links = Arrays.asList(
	            new Link("fighter", "/shipyard/module/fighter", "Fighter"),
	            new Link("cruiser", "/shipyard/module/cruiser", "Cruiser"),
	            new Link("destroyer", "/shipyard/module/destroyer", "Destroyer"),	
	            new Link("titan", "/shipyard/module/titan", "Titan")
	        );
	    model.addAttribute("types", links);
	}

	@Override
	public void prepareShipTable(Model model, String type) {
		shipyardData = API.shipyard().shipyard();
		
		List<TableHeader> headers = new ArrayList<>();
		headers.add(TableHeader.builder()
				.label("ID")
				.className("id-column")
				.build());
		headers.add(TableHeader.builder()
				.label("Name")
				.build());
		headers.add(TableHeader.builder()
				.label("Created")
				.build());
		headers.add(TableHeader.builder()
				.label("Last Modified")
				.build());
		
		List<TableRow> rows = new ArrayList<>();
		for (ShipBlueprint shipBlueprint : shipyardData.getShipBlueprints()) {
			ShipCore core = (ShipCore) ShipyardUtility.getShipComponentByType(shipBlueprint, ShipComponentType.CORE);
			if (core.getType().toString().equalsIgnoreCase(type)) {
				List<TableCell> cells = new ArrayList<>();
				cells.add(TableCell.builder().value(String.valueOf(shipBlueprint.getId())).className("id-column").build());
				cells.add(TableCell.builder().value(shipBlueprint.getModel()).className("ship-link").attributes("param-modal=shipyard-shipoverviewmodal, param=" + shipBlueprint.getModel().toLowerCase()).build());
				cells.add(TableCell.builder().value(StringUtils.formatDate(shipBlueprint.getCreated())).build());
				cells.add(TableCell.builder().value(StringUtils.formatDate(shipBlueprint.getLastModified())).build());
				rows.add(TableRow.builder()
						.cells(cells)
						.build());
			}
		}
		
    	model.addAttribute("ships", Table.builder()
    			.headers(headers)
    			.rows(rows)
				.build());
	}

	@Override
	public ShipCreateResponse createShip(ShipCreateRequest request) {
		ShipCreateResponse response = API.shipyard().createShip(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}
	
	@Override
	public ComponentCreateResponse createComponent(ComponentCreateRequest request) {
		ComponentCreateResponse response = API.shipyard().createComponent(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}
	
	@Override
	public ComponentUpdateResponse updateComponent(ComponentUpdateRequest request) {
		ComponentUpdateResponse response = API.shipyard().updateComponent(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}
	
	@Override
	public ComponentDeleteResponse deleteComponent(ComponentDeleteRequest request) {
		ComponentDeleteResponse response = API.shipyard().deleteComponent(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}

	@Override
	public void prepareOverviewModal(Model model, String shipname) {
		model.addAttribute("name", shipname);
    	prepareComponentsTable(model, shipname);
	}

	private void prepareComponentsTable(Model model, String shipname) {
		List<TableHeader> headers = new ArrayList<>();
		headers.add(TableHeader.builder()
				.label("ID")
				.className("id-column")
				.build());
		headers.add(TableHeader.builder()
				.label("Component")
				.build());
		headers.add(TableHeader.builder()
				.label("Type")
				.build());
		headers.add(TableHeader.builder()
		        .label("")
		        .build());
		
		List<TableRow> rows = new ArrayList<>();
		for (ShipBlueprint shipBlueprint : shipyardData.getShipBlueprints()) {
			if (shipBlueprint.getModel().equalsIgnoreCase(shipname)) {
				for (ShipComponent component : shipBlueprint.getComponents()) {
					List<TableCell> cells = new ArrayList<>();
					String componentType = ShipyardUtility.getShipComponentType(component);
					String componentSubtype = ShipyardUtility.getShipComponentSubtype(component);
					cells.add(TableCell.builder().value(String.valueOf(component.getId())).className("id-column").build());
					cells.add(TableCell.builder().value(componentType).build());
					cells.add(TableCell.builder().value(componentSubtype).build());
					cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("attribute-actions").build());
					rows.add(TableRow.builder()
							.cells(cells)
							.className("component-row")
							.attributes("data-component-name=" + componentType + ", data-component-id=" + component.getId() + ", data-shipname=" + shipname)
							.build());
				}
			}
		}
		
    	model.addAttribute("components", Table.builder()
    			.headers(headers)
    			.rows(rows)
				.build());
    	
    	prepareComponentLevelTable(model, shipname, 0);
	}
	
	@Override
	public void prepareComponentLevelTable(Model model, String shipname, int componentID) {
		List<TableHeader> headers = new ArrayList<>();
		headers.add(TableHeader.builder()
				.label("Level")
				.className("id-column")
				.build());
		headers.add(TableHeader.builder()
				.label("Type")
				.build());
		headers.add(TableHeader.builder()
				.label("Value")
				.build());
		
		List<TableRow> rows = new ArrayList<>();
		for (ShipBlueprint shipBlueprint : shipyardData.getShipBlueprints()) {
			if (shipBlueprint.getModel().equalsIgnoreCase(shipname)) {
				for (ShipComponent component : shipBlueprint.getComponents()) {
					if (component.getId() == componentID) {
						for (ComponentLevel componentLevel : component.getLevels()) {
							List<TableCell> cells = new ArrayList<>();
							cells.add(TableCell.builder().value(String.valueOf(componentLevel.getLevel())).className("id-column").build());
							cells.add(TableCell.builder().value(ShipyardUtility.getComponentLevelType(componentLevel)).build());
							cells.add(TableCell.builder().value(String.valueOf(componentLevel.getRequirementValue())).build());
							rows.add(TableRow.builder()
									.cells(cells)
									.className("level-row")
									.attributes("data-shipname=" + shipname + ", data-component-id=" + component.getId() + ", data-level=" + String.valueOf(componentLevel.getLevel()))
									.build());
						}
					}
				}
			}
		}
		
    	model.addAttribute("componentlevels", Table.builder()
    			.headers(headers)
    			.rows(rows)
				.build());
	}

	@Override
	public void prepareLevelAttributeTable(Model model, String shipname, int componentID, int level) {
		List<TableHeader> headers = new ArrayList<>();
		headers.add(TableHeader.builder()
				.label("ID")
				.className("id-column")
				.build());
		headers.add(TableHeader.builder()
				.label("Attribute")
				.build());
		headers.add(TableHeader.builder()
				.label("Value")
				.build());
		headers.add(TableHeader.builder()
				.label("")
				.build());
		
		List<TableRow> rows = new ArrayList<>();
		for (ShipBlueprint shipBlueprint : shipyardData.getShipBlueprints()) {
			if (shipBlueprint.getModel().equalsIgnoreCase(shipname)) {
				for (ShipComponent component : shipBlueprint.getComponents()) {
					if (component.getId() == componentID) {
						for (ComponentLevel componentLevel : component.getLevels()) {
							if (componentLevel.getLevel() == level) {
								for (ComponentAttribute attribute : componentLevel.getAttributes()) {
									List<TableCell> cells = new ArrayList<>();
									cells.add(TableCell.builder().value(String.valueOf(attribute.getId())).className("id-column").build());
									ShipyardUtility.getAttributeOptions(component);
									cells.add(TableCell.builder().value(ShipyardUtility.getAttributeName(attribute)).className("attribute-name").attributes("attribute-name-options=" + "test-test123").build());
									cells.add(TableCell.builder().value(String.valueOf(attribute.getValue())).className("attribute-value").build());
									cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("attribute-actions").build());
									rows.add(TableRow.builder()
											.cells(cells)
											.className("attribute-row")
											.attributes("data-attribute=" + ShipyardUtility.getAttributeName(attribute) + ", data-value=" + String.valueOf(attribute.getValue()))
											.build());
								}
							}
						}
					}
				}
			}
		}
		
		model.addAttribute("levelattributes", Table.builder()
    			.headers(headers)
    			.rows(rows)
				.build());
	}

	@Override
	public List<String> getComponentTypes(String type) {
		List<String> types = switch (type.toUpperCase()) {
        	case "CORE" -> Arrays.stream(CoreType.values()).map(Enum::name).toList();
        	case "WEAPON" -> Arrays.stream(WeaponType.values()).map(Enum::name).toList();
        	case "ENGINE" -> Arrays.stream(EngineType.values()).map(Enum::name).toList();
        	case "SHIELD" -> Arrays.stream(ShieldType.values()).map(Enum::name).toList();
        	case "HULL" -> Arrays.stream(HullType.values()).map(Enum::name).toList();
        	default -> List.of(" ");
		};
		return types;
	}
	
}
