package de.instinct.api.meta.dto;

import java.util.List;

import de.instinct.api.commander.dto.CommanderData;
import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.PlayerShipData;
import lombok.Data;

@Dto
@Data
public class LoadoutData {
	
	private CommanderData commander;
	private Infrastructure infrastructure;
	private List<PlayerShipData> ships;

}
