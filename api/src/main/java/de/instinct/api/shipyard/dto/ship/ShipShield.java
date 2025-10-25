package de.instinct.api.shipyard.dto.ship;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.level.ShieldLevel;
import de.instinct.api.shipyard.dto.ship.component.types.shield.ShieldType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShipShield extends ShipComponent {
	
	private ShieldType type;
	private List<ShieldLevel> levels;

}
