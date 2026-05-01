package de.instinct.mining.service.model;

import com.esotericsoftware.kryonet.Connection;

import de.instinct.api.mining.dto.player.MiningPlayerData;
import lombok.Data;

@Data
public class MiningClient {
	
	private String uuid;
	private String name;
	private int playerId;
	private Connection connection;
	private MiningPlayerData playerData;

}
