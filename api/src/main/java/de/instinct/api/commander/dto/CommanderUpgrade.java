package de.instinct.api.commander.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class CommanderUpgrade {

	private CommanderStat stat;
	private float value;
	
}
