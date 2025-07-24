package de.instinct.api.shipyard.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.meta.dto.ResourceAmount;
import lombok.Data;

@Dto
@Data
public class ShipLevel {
	
	private List<ResourceAmount> cost;
	private List<ShipStatChange> statEffects;

}
