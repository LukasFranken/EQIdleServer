package de.instinct.api.commander.dto;

public enum CommanderStat {
	
	MAX_RES("Max RES"),
	START_RES("Start RES"),
	RES_PER_SECOND("RES/s");
	
	private String label;
	
	CommanderStat(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

}
