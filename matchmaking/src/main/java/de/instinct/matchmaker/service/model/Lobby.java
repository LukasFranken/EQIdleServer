package de.instinct.matchmaker.service.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Lobby {
	
	private String lobbyUUID;
	private GameType type;
	private List<String> playerUUIDs;

}
