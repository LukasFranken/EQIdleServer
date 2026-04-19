package de.instinct.engine_api.core.model;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.engine.core.player.Player;
import lombok.Data;

@Dto
@Data
public class GameStateInitialization {
	
	private String gameUUID;
	private List<Player> players;
	private long pauseTimeLimitMS;
	private int pauseCountLimit;
	private GameMap map;

}