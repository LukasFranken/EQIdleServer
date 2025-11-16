package de.instinct.api.construction.dto;

import de.instinct.api.core.annotation.Dto;
import de.instinct.engine.model.ship.components.types.WeaponType;
import lombok.Data;

@Dto
@Data
public class PlanetWeapon {
	
	private WeaponType type;
	private float damage;
	private float range;
	private float speed;
	private long cooldown;

}
