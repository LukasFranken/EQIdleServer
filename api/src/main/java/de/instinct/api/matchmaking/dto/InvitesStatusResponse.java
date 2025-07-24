package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.matchmaking.model.Invite;
import lombok.Data;

@Dto
@Data
public class InvitesStatusResponse {
	
	private List<Invite> invites;

}
