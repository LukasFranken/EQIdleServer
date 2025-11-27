package de.instinct.control.service.shipyard;

import java.util.List;

import org.springframework.ui.Model;

import de.instinct.api.shipyard.dto.admin.ComponentCreateRequest;
import de.instinct.api.shipyard.dto.admin.ComponentCreateResponse;
import de.instinct.api.shipyard.dto.admin.ComponentDeleteRequest;
import de.instinct.api.shipyard.dto.admin.ComponentDeleteResponse;
import de.instinct.api.shipyard.dto.admin.ShipCreateRequest;
import de.instinct.api.shipyard.dto.admin.ShipCreateResponse;
import de.instinct.control.service.ModelService;

public interface ShipyardService extends ModelService {

	ShipCreateResponse createShip(ShipCreateRequest request);
	
	ComponentCreateResponse createComponent(ComponentCreateRequest request);
	
	ComponentDeleteResponse deleteComponent(ComponentDeleteRequest request);
	
	void prepareShipTable(Model model, String type);

	void prepareOverviewModal(Model model, String shipname);

	void prepareComponentLevelTable(Model model, String shipname, int componentID);

	void prepareLevelAttributeTable(Model model, String shipname, int componentID, int level);

	List<String> getComponentTypes(String type);

}
