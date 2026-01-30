package de.instinct.engine_api.core.model;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.engine.model.Player;
import lombok.Data;

@Dto
@Data
public class GameStateInitialization {
	
	private String gameUUID;
	private GameMap map;
	private List<Player> players;
	private float ancientPlanetResourceDegradationFactor;
	private int gameTimeLimitMS;
	private int atpToWin;
	private long pauseTimeLimitMS;
	private int pauseCountLimit;

}