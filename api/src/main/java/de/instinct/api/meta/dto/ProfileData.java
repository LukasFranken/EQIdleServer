package de.instinct.api.meta.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ProfileData {
	
	private String username;
	private UserRank userRank;
	private PlayerRank rank;
	private long currentExp;

}
