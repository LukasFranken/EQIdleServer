package de.instinct.control.service.shipyard.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import de.instinct.api.control.model.Link;
import de.instinct.api.control.model.Table;
import de.instinct.api.control.model.TableCell;
import de.instinct.api.control.model.TableHeader;
import de.instinct.api.control.model.TableRow;
import de.instinct.api.core.API;
import de.instinct.api.meta.dto.Resource;
import de.instinct.api.meta.dto.ResourceAmount;
import de.instinct.api.shipyard.dto.ShipyardData;
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
import de.instinct.api.shipyard.dto.ship.ShipBlueprint;
import de.instinct.api.shipyard.dto.ship.ShipComponent;
import de.instinct.api.shipyard.dto.ship.ShipCore;
import de.instinct.api.shipyard.dto.ship.component.ComponentAttribute;
import de.instinct.api.shipyard.dto.ship.component.ComponentLevel;
import de.instinct.api.shipyard.dto.ship.component.ShipComponentType;
import de.instinct.api.shipyard.dto.ship.component.types.core.CoreAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.core.CoreRequirementType;
import de.instinct.api.shipyard.dto.ship.component.types.engine.EngineAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.engine.EngineRequirementType;
import de.instinct.api.shipyard.dto.ship.component.types.hull.HullAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.hull.HullRequirementType;
import de.instinct.api.shipyard.dto.ship.component.types.shield.ShieldAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.shield.ShieldRequirementType;
import de.instinct.api.shipyard.dto.ship.component.types.weapon.WeaponAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.weapon.WeaponRequirementType;
import de.instinct.api.shipyard.service.impl.ShipyardUtility;
import de.instinct.control.service.shipyard.ShipyardControlService;
import de.instinct.engine.model.ship.components.types.CoreType;
import de.instinct.engine.model.ship.components.types.EngineType;
import de.instinct.engine.model.ship.components.types.HullType;
import de.instinct.engine.model.ship.components.types.ShieldType;
import de.instinct.engine.model.ship.components.types.WeaponType;
import de.instinct.eqspringutils.StringUtils;

@Service
public class ShipyardServiceImpl implements ShipyardControlService {
	
	private ShipyardData shipyardData;

	@Override
	public void setModel(Model model) {
		List<Link> links = new ArrayList<>();
		for (CoreType coreType : CoreType.values()) {
			links.add(new Link(coreType.toString().toLowerCase(), "/shipyard/module/" + coreType.toString().toLowerCase(), coreType.toString()));
		}
	    model.addAttribute("types", links);
	}

	@Override
	public ShipCreateResponse createShip(ShipCreateRequest request) {
		ShipCreateResponse response = API.shipyard().createShip(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}
	
	@Override
	public BuildCostCreateResponse createBuildCost(BuildCostCreateRequest request) {
		BuildCostCreateResponse response = API.shipyard().createBuildCost(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}

	@Override
	public BuildCostUpdateResponse updateBuildCost(BuildCostUpdateRequest request) {
		BuildCostUpdateResponse response = API.shipyard().updateBuildCost(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}

	@Override
	public BuildCostDeleteResponse deleteBuildCost(BuildCostDeleteRequest request) {
		BuildCostDeleteResponse response = API.shipyard().deleteBuildCost(request);
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
	public ComponentLevelCreateResponse createComponentLevel(ComponentLevelCreateRequest request) {
		ComponentLevelCreateResponse response = API.shipyard().createComponentLevel(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}
	
	@Override
	public ComponentLevelUpdateResponse updateComponentLevel(ComponentLevelUpdateRequest request) {
		ComponentLevelUpdateResponse response = API.shipyard().updateComponentLevel(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}

	@Override
	public ComponentLevelDeleteResponse deleteComponentLevel(ComponentLevelDeleteRequest request) {
		ComponentLevelDeleteResponse response = API.shipyard().deleteComponentLevel(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}
	
	@Override
	public LevelAttributeCreateResponse createLevelAttribute(LevelAttributeCreateRequest request) {
		LevelAttributeCreateResponse response = API.shipyard().createLevelAttribute(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}

	@Override
	public LevelAttributeUpdateResponse updateLevelAttribute(LevelAttributeUpdateRequest request) {
		LevelAttributeUpdateResponse response = API.shipyard().updateLevelAttribute(request);
		shipyardData = API.shipyard().shipyard();
		return response;
	}

	@Override
	public LevelAttributeDeleteResponse deleteLevelAttribute(LevelAttributeDeleteRequest request) {
		LevelAttributeDeleteResponse response = API.shipyard().deleteLevelAttribute(request);
		shipyardData = API.shipyard().shipyard();
		return response;
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
		headers.add(TableHeader.builder()
				.label("Build Cost")
				.build());
		
		List<TableRow> rows = new ArrayList<>();
		for (ShipBlueprint shipBlueprint : shipyardData.getShipBlueprints()) {
			ShipCore core = (ShipCore) ShipyardUtility.getShipComponentByType(shipBlueprint, ShipComponentType.CORE);
			if (core.getType().toString().equalsIgnoreCase(type)) {
				List<TableCell> cells = new ArrayList<>();
				cells.add(TableCell.builder().value(String.valueOf(shipBlueprint.getId())).className("id-column").build());
				cells.add(TableCell.builder().value(shipBlueprint.getModel()).className("ship-link").attributes("param-modal=shipyard-shipoverviewmodal, param=" + shipBlueprint.getModel().toLowerCase() + ", init-method=initializeBlueprintModal").build());
				cells.add(TableCell.builder().value(StringUtils.formatDate(shipBlueprint.getCreated())).build());
				cells.add(TableCell.builder().value(StringUtils.formatDate(shipBlueprint.getLastModified())).build());
				cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("ship-actions").attributes("param-modal=shipyard-buildcostmodal, param=" + shipBlueprint.getModel().toLowerCase() + ", init-method=initializeBuildCostModal").build());
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
	public void prepareBuildCostModal(Model model, String shipname) {
		model.addAttribute("name", shipname.toUpperCase());
    	prepareBuildCostTable(model, shipname);
	}

	private void prepareBuildCostTable(Model model, String shipname) {
		List<TableHeader> headers = new ArrayList<>();
		headers.add(TableHeader.builder()
				.label("ID")
				.className("id-column")
				.build());
		headers.add(TableHeader.builder()
				.label("Resource")
				.build());
		headers.add(TableHeader.builder()
				.label("Amount")
				.build());
		headers.add(TableHeader.builder()
		        .label("")
		        .build());
		
		List<TableRow> rows = new ArrayList<>();
		for (ShipBlueprint shipBlueprint : shipyardData.getShipBlueprints()) {
			if (shipBlueprint.getModel().equalsIgnoreCase(shipname)) {
				for (ResourceAmount resourceCost : shipBlueprint.getBuildCost()) {
					List<TableCell> cells = new ArrayList<>();
					cells.add(TableCell.builder().value(String.valueOf(resourceCost.getId())).className("id-column").build());
					cells.add(TableCell.builder().value(resourceCost.getType().toString()).build());
					cells.add(TableCell.builder().value(String.valueOf(resourceCost.getAmount())).build());
					cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("buildcost-actions").build());
					rows.add(TableRow.builder()
							.cells(cells)
							.className("buildcost-row")
							.attributes("data-shipname=" + shipname)
							.build());
				}
			}
		}
		
    	model.addAttribute("buildcosts", Table.builder()
    			.headers(headers)
    			.rows(rows)
				.build());
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
					cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("component-actions").build());
					rows.add(TableRow.builder()
							.cells(cells)
							.className("component-row")
							.attributes("data-component-name=" + componentType + ", data-component-id=" + component.getId() + ", data-component-type=" + ShipyardUtility.getShipComponentSubtype(component) + ", data-shipname=" + shipname)
							.build());
				}
			}
		}
		
    	model.addAttribute("components", Table.builder()
    			.headers(headers)
    			.rows(rows)
				.build());
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
		headers.add(TableHeader.builder()
		        .label("")
		        .build());
		
		List<TableRow> rows = new ArrayList<>();
		for (ShipBlueprint shipBlueprint : shipyardData.getShipBlueprints()) {
			if (shipBlueprint.getModel().equalsIgnoreCase(shipname)) {
				for (ShipComponent component : shipBlueprint.getComponents()) {
					if (component.getId() == componentID) {
						String componentType = ShipyardUtility.getShipComponentType(component);
						for (ComponentLevel componentLevel : component.getLevels()) {
							List<TableCell> cells = new ArrayList<>();
							cells.add(TableCell.builder().value(String.valueOf(componentLevel.getLevel())).className("id-column").build());
							cells.add(TableCell.builder().value(ShipyardUtility.getComponentLevelType(componentLevel)).build());
							cells.add(TableCell.builder().value(String.valueOf(componentLevel.getRequirementValue())).build());
							cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("component-level-actions").build());
							rows.add(TableRow.builder()
									.cells(cells)
									.className("component-level-row")
									.attributes("data-component-name=" + componentType + ", data-shipname=" + shipname + ", data-component-id=" + component.getId() + ", data-level=" + String.valueOf(componentLevel.getLevel()))
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
						String componentType = ShipyardUtility.getShipComponentType(component);
						for (ComponentLevel componentLevel : component.getLevels()) {
							if (componentLevel.getLevel() == level) {
								for (ComponentAttribute attribute : componentLevel.getAttributes()) {
									List<TableCell> cells = new ArrayList<>();
									cells.add(TableCell.builder().value(String.valueOf(attribute.getId())).className("id-column").build());
									ShipyardUtility.getAttributeOptions(component);
									cells.add(TableCell.builder().value(ShipyardUtility.getAttributeName(attribute)).className("attribute-name").attributes("attribute-name-options=" + "test-test123").build());
									cells.add(TableCell.builder().value(String.valueOf(attribute.getValue())).className("attribute-value").build());
									cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("level-attribute-actions").build());
									rows.add(TableRow.builder()
											.cells(cells)
											.className("attribute-row")
											.attributes("data-component-name=" + componentType + ",data-attribute=" + ShipyardUtility.getAttributeName(attribute) + ", data-value=" + String.valueOf(attribute.getValue()) + ", data-level=" + String.valueOf(componentLevel.getLevel()))
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
	public List<String> getResourceTypes() {
		return Arrays.stream(Resource.values()).map(Enum::name).toList();
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

	@Override
	public List<String> getComponentLevelTypes(String type) {
		List<String> types = switch (type.toUpperCase()) {
    		case "CORE" -> Arrays.stream(CoreRequirementType.values()).map(Enum::name).toList();
    		case "WEAPON" -> Arrays.stream(WeaponRequirementType.values()).map(Enum::name).toList();
    		case "ENGINE" -> Arrays.stream(EngineRequirementType.values()).map(Enum::name).toList();
    		case "SHIELD" -> Arrays.stream(ShieldRequirementType.values()).map(Enum::name).toList();
    		case "HULL" -> Arrays.stream(HullRequirementType.values()).map(Enum::name).toList();
    		default -> List.of(" ");
		};
		return types;
	}

	@Override
	public List<String> getLevelAttributeTypes(String type) {
		List<String> types = switch (type.toUpperCase()) {
		case "CORE" -> Arrays.stream(CoreAttributeType.values()).map(Enum::name).toList();
		case "WEAPON" -> Arrays.stream(WeaponAttributeType.values()).map(Enum::name).toList();
		case "ENGINE" -> Arrays.stream(EngineAttributeType.values()).map(Enum::name).toList();
		case "SHIELD" -> Arrays.stream(ShieldAttributeType.values()).map(Enum::name).toList();
		case "HULL" -> Arrays.stream(HullAttributeType.values()).map(Enum::name).toList();
		default -> List.of(" ");
	};
	return types;
	}
	
}
