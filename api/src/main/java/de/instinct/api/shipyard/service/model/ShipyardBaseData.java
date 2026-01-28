package de.instinct.api.shipyard.service.model;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShipyardBaseData {
	
	private int baseSlots;
	private int baseActiveShipSlots;
	private int currentShipId;
	private List<String> blueprintTags;

}
