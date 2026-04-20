package de.instinct.engine_api.mining;

import java.util.ArrayList;

import de.instinct.engine.core.order.GameOrder;
import de.instinct.engine.mining.MiningEngine;
import de.instinct.engine.mining.data.MiningGameState;
import de.instinct.engine.mining.entity.data.MiningEntityData;
import de.instinct.engine.mining.entity.ship.MiningPlayerShip;
import de.instinct.engine.mining.entity.ship.data.MiningShipData;
import de.instinct.engine.mining.player.MiningPlayer;
import de.instinct.engine_api.core.service.GameStateInitializer;
import de.instinct.engine_api.mining.model.MiningGameStateInitialization;

public class MiningEngineInterface extends GameStateInitializer {
	
	private MiningEngine engine;

	public MiningEngineInterface() {
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
		player.shipData = new MiningShipData();
		player.shipData.coreCharge = 20f;
		
		player.shipData.cargoCapacity = 20f;
		
		player.shipData.acceleration = 20f;
		player.shipData.deceleration = 10f;
		player.shipData.maxSpeed = 200f;
		player.shipData.maxSpeedReverse = -50f;
		player.shipData.rotationAcceleration = 10f;
		player.shipData.maxRotationSpeed = 40f;
		player.shipData.chargePerSecond = 0.2f;
		player.shipData.inertiaDampening = 50f;
		
		player.shipData.cooldownMS = 500;
		player.shipData.lifetimeMS = 2000;
		player.shipData.damage = 5f;
		player.shipData.projectileSpeed = 10f;
		player.shipData.chargePerShot = 0.5f;
		return player;
	}
	
	public void integrateOrder(MiningGameState gameState, GameOrder order) {
		engine.queue(gameState, order);
	}
	
	public void addIntegratedOrder(MiningGameState gameState, GameOrder order) {
		gameState.orderData.processedOrders.add(order);
	}

	public GameOrder getLastOrder(MiningGameState state) {
		if (state.orderData.processedOrders.isEmpty()) return null;
		return state.orderData.processedOrders.get(state.orderData.processedOrders.size() - 1);
	}
	
	public boolean checkRecalled(MiningGameState state) {
		for (MiningPlayerShip ship : state.entityData.playerShips) {
			if (!ship.recalled) {
				return false;
			}
		}
		return true;
	}

	public boolean checkFailed(MiningGameState state) {
		//check if all ships are destroyed or out of fuel
		return false;
	}

	public void updateGameState(MiningGameState state, long deltaTimeMS) {
		engine.update(state, deltaTimeMS);
	}
	
}
