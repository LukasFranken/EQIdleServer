package de.instinct.engine_api.fleet.model;

import de.instinct.engine_api.core.model.GameStateInitialization;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FleetGameStateInitialization extends GameStateInitialization {
	
	private int atpToWin;
	private int gameTimeLimitMS;

}
