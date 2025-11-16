package de.instinct.control.service.shipyard;

import org.springframework.ui.Model;

import de.instinct.api.shipyard.dto.admin.ShipCreateRequest;
import de.instinct.api.shipyard.dto.admin.ShipCreateResponse;
import de.instinct.control.service.ModelService;

public interface ShipyardService extends ModelService {
	
	void prepareShipTable(Model model, String module);

	ShipCreateResponse createShip(ShipCreateRequest request);

	void prepareOverviewModal(String shipname, Model model);

}
