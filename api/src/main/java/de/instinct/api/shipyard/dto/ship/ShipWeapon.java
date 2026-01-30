package de.instinct.api.shipyard.dto.ship;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.types.ShipWeaponType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShipWeapon extends ShipComponent {
	
	private ShipWeaponType type;

}
