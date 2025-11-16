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
import de.instinct.api.shipyard.dto.ship.ShipBlueprint;
import de.instinct.api.shipyard.dto.ship.ShipCore;
import de.instinct.api.shipyard.service.impl.ShipyardUtility;
import de.instinct.api.shipyard.service.model.ShipComponentType;
import de.instinct.control.service.base.model.Link;
import de.instinct.control.service.shipyard.ShipyardService;
import de.instinct.control.service.shipyard.model.ShipTableItem;
import de.instinct.eqspringutils.StringUtils;

@Service
public class ShipyardServiceImpl implements ShipyardService {

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
	public void prepareShipTable(Model model, String module) {
		ShipyardData shipyardData = API.shipyard().shipyard();
		List<ShipTableItem> shipTableItems = new ArrayList<>();
		for (ShipBlueprint shipBlueprint : shipyardData.getShipBlueprints()) {
			ShipCore core = (ShipCore) ShipyardUtility.getShipComponentByType(shipBlueprint, ShipComponentType.CORE);
			if (core.getType().toString().equalsIgnoreCase(module)) {
				shipTableItems.add(ShipTableItem.builder()
						.id(shipBlueprint.getId())
						.name(shipBlueprint.getModel())
						.created(StringUtils.formatDate(shipBlueprint.getCreated()))
						.lastModified(StringUtils.formatDate(shipBlueprint.getLastModified()))
						.build());
			}
		}
		model.addAttribute("ships", shipTableItems);
	}

	@Override
	public ShipCreateResponse createShip(ShipCreateRequest request) {
		return API.shipyard().createShip(request);
	}

	@Override
	public void prepareOverviewModal(String shipname, Model model) {
		model.addAttribute("name", shipname);
	}
	
}
