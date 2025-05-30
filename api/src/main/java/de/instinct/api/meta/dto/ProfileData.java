package de.instinct.api.meta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileData {
	
	private String username;
	private PlayerRank rank;
	private UserRank userRank;
	private long currentExp;

}
