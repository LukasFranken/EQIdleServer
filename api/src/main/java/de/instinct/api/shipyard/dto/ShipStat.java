package de.instinct.api.shipyard.dto;

public enum ShipStat {
	
	MOVEMENT_SPEED("Move Speed"),
	COST("Resource Cost"),
	COMMAND_POINTS_COST("CP Cost"),
	WEAPON_DAMAGE("Damage"),
	WEAPON_AOE_RADIUS("Explosion Radius"),
	WEAPON_RANGE("Range"),
	WEAPON_COOLDOWN("Shot Cooldown"),
	WEAPON_PROJECTILE_SPEED("Projectile Speed"),
	DEFENSE_ARMOR("Armor"),
	DEFENSE_SHIELD("Shield"),
	DEFENSE_SHIELD_REGENERATION_SPEED("Shield reg. /s");
	
	private final String label;
	
	ShipStat(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

}
