package de.instinct.api.construction.dto;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.types.ShipWeaponType;
import lombok.Data;

@Dto
@Data
public class PlanetWeapon {
	
	private ShipWeaponType type;
	private float damage;
	private float range;
	private float speed;
	private long cooldown;

}
