package de.instinct.api.meta.dto;

import java.util.List;

import de.instinct.api.commander.dto.CommanderData;
import de.instinct.api.construction.dto.PlayerInfrastructure;
import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.PlayerShipData;
import lombok.Data;

@Dto
@Data
public class LoadoutData {
	
	private CommanderData commander;
	private PlayerInfrastructure playerInfrastructure;
	private List<PlayerShipData> ships;

}
