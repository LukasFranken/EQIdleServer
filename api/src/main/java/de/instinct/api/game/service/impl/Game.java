package de.instinct.api.game.service.impl;

import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.game.service.GameInterface;

public class Game extends BaseService implements GameInterface {

	public Game() {
		super("game");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}

}
