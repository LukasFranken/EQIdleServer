package de.instinct.api.social.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class FriendRequest {
	
	private String fromName;
	private long timestamp;

}
