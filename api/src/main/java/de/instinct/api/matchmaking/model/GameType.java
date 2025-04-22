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
	
	public VersusMode versusMode;
	public GameMode gameMode;
	public FactionMode factionMode;
	
	public boolean matches(GameType otherGameType) {
		if (versusMode != otherGameType.versusMode) return false;
		if (gameMode != otherGameType.gameMode) return false;
		if (factionMode != otherGameType.factionMode) return false;
		return true;
	}

}
