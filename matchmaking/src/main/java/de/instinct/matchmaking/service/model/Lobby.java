package de.instinct.matchmaking.service.model;

import java.util.List;

import de.instinct.api.matchmaking.model.GameType;
import de.instinct.api.matchmaking.model.Invite;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Lobby {
	
	private String lobbyUUID;
	private GameType type;
	private List<String> userUUIDs;
	private GameserverInfo gameserverInfo;
	private List<Invite> invites;

}
