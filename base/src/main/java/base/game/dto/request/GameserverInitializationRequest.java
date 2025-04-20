package base.game.dto.request;

import java.util.List;

import base.game.model.GameType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameserverInitializationRequest {
	
	private String lobbyUUID;
	private List<String> playerUUIDs;
	private GameType type;

}
