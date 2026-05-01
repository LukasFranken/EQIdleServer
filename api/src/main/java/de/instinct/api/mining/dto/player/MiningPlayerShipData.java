package de.instinct.api.mining.dto.player;

import de.instinct.api.core.annotation.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Dto
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiningPlayerShipData {
	
	private int stage;
	
	private float coreCharge;
	
	private MiningShipWeaponType weaponType;
	private long cooldownMS;
	private float damage;
	private float projectileSpeed;
	private float chargePerShot;
	private long lifetimeMS;
	
	private float acceleration;
	private float maxSpeed;
	private float deceleration;
	private float maxReverseSpeed;
	private float rotationAcceleration;
	private float maxRotationSpeed;
	private float chargePerSecond;
	private float inertiaDampening;
	
	private float cargoCapacity;

}
