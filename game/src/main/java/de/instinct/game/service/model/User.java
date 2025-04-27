package de.instinct.game.service.model;

import com.esotericsoftware.kryonet.Connection;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
	
	private String uuid;
	private int playerId;
	private Connection connection;

}
