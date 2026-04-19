package de.instinct.mining.service.model;

import java.util.List;

import de.instinct.engine.mining.data.MiningGameState;
import lombok.Data;

@Data
public class Session {
	
	private MiningGameState state;
	private List<MiningClient> clients;

}
