package de.instinct.api.shipyard.dto;

import de.instinct.api.construction.dto.WeaponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipWeapon {
	
	private WeaponType type;
	private float damage;
	private float range;
	private float speed;
	private long cooldown;

}
