package de.instinct.engine_api.mining;

import java.util.ArrayList;

import de.instinct.engine.mining.MiningEngine;
import de.instinct.engine.mining.data.MiningGameState;
import de.instinct.engine.mining.entity.data.MiningEntityData;
import de.instinct.engine_api.core.service.GameStateInitializer;
import de.instinct.engine_api.mining.model.MiningGameStateInitialization;

public class MiningGameStateInitializer extends GameStateInitializer {
	
	private MiningEngine engine;

	public MiningGameStateInitializer() {
		this.engine = new MiningEngine();
	}
	
	public MiningGameState initializeMining(MiningGameStateInitialization initialization) {
		MiningGameState state = new MiningGameState();
		super.initialize(state, initialization);
		
		state.recallRadius = 50f;
		state.entityData = new MiningEntityData();
		state.entityData.projectiles = new ArrayList<>();
		state.entityData.playerShips = new ArrayList<>();
		state.entityData.asteroids = new ArrayList<>();
		
		engine.initialize(state);
		return state;
	}
}
