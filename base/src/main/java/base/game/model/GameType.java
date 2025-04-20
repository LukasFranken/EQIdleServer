package base.game.model;

import base.game.model.enums.FactionMode;
import base.game.model.enums.GameMode;
import base.game.model.enums.VersusMode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
