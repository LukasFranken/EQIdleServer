package de.instinct.api.matchmaking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invite {
	
	private String lobbyUUID;
	private String fromUsername;
	private String toUsername;
	private String toUUID;
	private GameType selectedGameType;

}
