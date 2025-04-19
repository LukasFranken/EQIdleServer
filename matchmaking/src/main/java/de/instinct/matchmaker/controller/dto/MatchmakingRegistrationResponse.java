package de.instinct.matchmaker.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchmakingRegistrationResponse {
	
	private MatchmakingRegistrationResponseCode code;
	private String lobbyUUID;

}
