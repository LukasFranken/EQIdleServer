package de.instinct.engine_api.mining;

import java.util.ArrayList;

import de.instinct.engine.mining.MiningEngine;
import de.instinct.engine.mining.data.MiningGameState;
import de.instinct.engine.mining.entity.data.MiningEntityData;
import de.instinct.engine.mining.player.MiningPlayer;
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
	
	public MiningPlayer getTestPlayer() {
		MiningPlayer player = new MiningPlayer();
		player.shipData.coreCharge = 20f;
		
		player.shipData.cargoCapacity = 20f;
		
		player.shipData.acceleration = 20f;
		player.shipData.deceleration = 10f;
		player.shipData.maxSpeed = 200f;
		player.shipData.maxSpeedReverse = -50f;
		player.shipData.rotationAcceleration = 20f;
		player.shipData.maxRotationSpeed = 50f;
		player.shipData.chargePerSecond = 0.2f;
		player.shipData.inertiaDampening = 50f;
		
		player.shipData.cooldownMS = 500;
		player.shipData.lifetimeMS = 2000;
		player.shipData.damage = 5f;
		player.shipData.projectileSpeed = 10f;
		player.shipData.chargePerShot = 0.5f;
		return player;
	}
}
