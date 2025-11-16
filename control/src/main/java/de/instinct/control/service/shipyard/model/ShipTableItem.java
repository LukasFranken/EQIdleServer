package de.instinct.control.service.shipyard.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipTableItem {
	
	private int id;
	private String name;
	private String created;
	private String lastModified;

}
