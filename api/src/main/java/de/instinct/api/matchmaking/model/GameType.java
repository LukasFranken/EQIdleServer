package de.instinct.api.matchmaking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameType {
	
	private String map;
	private int threatLevel;
	private long duration;
	private VersusMode versusMode;
	private GameMode gameMode;
	private FactionMode factionMode;
	
	public boolean matches(GameType otherGameType) {
		if (!sameMapAs(otherGameType.map)) return false;
		if (threatLevel != otherGameType.threatLevel) return false;
		if (duration != otherGameType.duration) return false;
		if (versusMode != otherGameType.versusMode) return false;
		if (gameMode != otherGameType.gameMode) return false;
		if (factionMode != otherGameType.factionMode) return false;
		return true;
	}

	private boolean sameMapAs(String otherMap) {
		if (map != null) {
			if (otherMap == null) return false;
			if (!map.equals(otherMap)) return false;
		}
		return true;
	}

}
