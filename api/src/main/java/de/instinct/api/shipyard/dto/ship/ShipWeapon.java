package de.instinct.api.shipyard.dto.ship;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.level.WeaponLevel;
import de.instinct.engine.model.ship.WeaponType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Dto
@Data
@EqualsAndHashCode(callSuper = false)
public class ShipWeapon extends ShipComponent {
	
	private WeaponType type;
	private List<WeaponLevel> levels;

}
