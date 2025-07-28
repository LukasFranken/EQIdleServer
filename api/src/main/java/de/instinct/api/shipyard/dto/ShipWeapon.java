package de.instinct.api.shipyard.dto;

import de.instinct.api.construction.dto.WeaponType;
import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShipWeapon {
	
	private WeaponType type;
	private float damage;
	private float range;
	private float aoeRadius;
	private float speed;
	private long cooldown;

}
