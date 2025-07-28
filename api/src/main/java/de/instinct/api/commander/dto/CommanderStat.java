package de.instinct.api.commander.dto;

public enum CommanderStat {
	
	MAX_CP("Max CP"),
	START_CP("Start CP"),
	CP_PER_SECOND("CP/s");
	
	private String label;
	
	CommanderStat(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

}
