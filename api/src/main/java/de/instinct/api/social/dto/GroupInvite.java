package de.instinct.api.social.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class GroupInvite {
	
	private String groupToken;
	private String fromName;
	private long timestamp;

}
