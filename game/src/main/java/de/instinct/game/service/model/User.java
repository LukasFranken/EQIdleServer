package de.instinct.game.service.model;

import com.esotericsoftware.kryonet.Connection;

import de.instinct.api.meta.dto.Loadout;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
	
	private String uuid;
	private String name;
	private int playerId;
	private int teamid;
	private Loadout loadout;
	private Connection connection;

}
