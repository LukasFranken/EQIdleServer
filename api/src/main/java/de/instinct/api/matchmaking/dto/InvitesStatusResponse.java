package de.instinct.api.matchmaking.dto;

import java.util.List;

import de.instinct.api.matchmaking.model.Invite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitesStatusResponse {
	
	private List<Invite> invites;

}
