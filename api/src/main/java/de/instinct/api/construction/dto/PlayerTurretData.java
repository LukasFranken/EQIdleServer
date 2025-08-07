package de.instinct.api.construction.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class PlayerTurretData {
	
	private String uuid;
	private int turretId;
	private boolean inUse;

}
