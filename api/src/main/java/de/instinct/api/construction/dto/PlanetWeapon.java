package de.instinct.api.construction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanetWeapon {
	
	private WeaponType type;
	private float damage;
	private float range;
	private float speed;
	private long cooldown;

}
