package de.instinct.matchmaking.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameserverInfo {
	
	private GameserverStatus status;
	private String gameSessionUUID;
	private String address;
	private int port;

}
