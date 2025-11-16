package de.instinct.shipyard.service.model;

import java.util.List;

import lombok.Data;

@Data
public class ShipyardBaseData {
	
	private int baseSlots;
	private int baseActiveShipSlots;
	private int currentShipId;
	private List<String> blueprintTags;

}
