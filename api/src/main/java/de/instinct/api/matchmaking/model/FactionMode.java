package de.instinct.api.matchmaking.model;

public enum FactionMode {
	
	ONE_VS_ONE(1),
	TWO_VS_TWO(2),
	THREE_VS_THREE(3);
	
	public int teamPlayerCount;
	
	private FactionMode(int teamPlayerCount) {
		this.teamPlayerCount = teamPlayerCount;
	}
	
	public int getTeamPlayerCount() {
		return teamPlayerCount;
	}
	
	public static FactionMode fromTeamPlayerCount(int teamPlayerCount) {
		for (FactionMode mode : values()) {
			if (mode.teamPlayerCount == teamPlayerCount) {
				return mode;
			}
		}
		throw new IllegalArgumentException("Invalid player count for faction mode: " + teamPlayerCount);
	}

}
