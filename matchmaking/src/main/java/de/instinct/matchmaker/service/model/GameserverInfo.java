package de.instinct.matchmaker.service.model;

import de.instinct.matchmaker.service.model.enums.GameserverStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameserverInfo {
	
	private GameserverStatus status;
	private String address;
	private int port;

}
