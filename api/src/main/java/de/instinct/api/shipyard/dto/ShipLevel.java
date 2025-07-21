package de.instinct.api.shipyard.dto;

import java.util.List;

import de.instinct.api.meta.dto.ResourceAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipLevel {
	
	private List<ResourceAmount> cost;
	private List<ShipStatChange> statEffects;

}
