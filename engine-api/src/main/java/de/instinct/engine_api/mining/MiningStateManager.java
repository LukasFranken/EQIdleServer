package de.instinct.engine_api.mining;

import java.util.ArrayList;

import de.instinct.api.mining.dto.player.MiningPlayerData;
import de.instinct.engine.core.order.GameOrder;
import de.instinct.engine.mining.MiningEngine;
import de.instinct.engine.mining.data.MiningGameState;
import de.instinct.engine.mining.entity.data.MiningEntityData;
import de.instinct.engine.mining.entity.ship.MiningPlayerShip;
import de.instinct.engine.mining.entity.ship.MiningPlayerShipProcessor;
import de.instinct.engine.mining.entity.ship.data.MiningShipData;
import de.instinct.engine.mining.player.MiningPlayer;
import de.instinct.engine_api.core.service.GameStateInitializer;
import de.instinct.engine_api.mining.model.MiningGameStateInitialization;

public class MiningStateManager extends GameStateInitializer {
	
	private MiningEngine engine;

	public MiningStateManager() {
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
		state.map = initialization.getMap();
		
		engine.initialize(state);
		return state;
	}
	
	public MiningPlayer getPlayer(MiningPlayerData data) {
		MiningPlayer player = new MiningPlayer();
		player.shipData = new MiningShipData();
		player.shipData.radius = 10f + (2f * data.getShipData().getStage());
		player.shipData.coreCharge = data.getShipData().getCoreCharge();
		
		player.shipData.cargoCapacity = data.getShipData().getCargoCapacity();
		
		player.shipData.acceleration = data.getShipData().getAcceleration();
		player.shipData.deceleration = data.getShipData().getDeceleration();
		player.shipData.maxSpeed = data.getShipData().getMaxSpeed();
		player.shipData.maxSpeedReverse = data.getShipData().getMaxReverseSpeed();
		player.shipData.rotationAcceleration = data.getShipData().getRotationAcceleration();
		player.shipData.maxRotationSpeed = data.getShipData().getMaxRotationSpeed();
		player.shipData.chargePerSecond = data.getShipData().getChargePerSecond();
		player.shipData.inertiaDampening = data.getShipData().getInertiaDampening();
		
		player.shipData.cooldownMS = data.getShipData().getCooldownMS();
		player.shipData.lifetimeMS = data.getShipData().getLifetimeMS();
		player.shipData.damage = data.getShipData().getDamage();
		player.shipData.projectileSpeed = data.getShipData().getProjectileSpeed();
		player.shipData.chargePerShot = data.getShipData().getChargePerShot();
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
	
	public boolean checkFinished(MiningGameState state) {
		for (MiningPlayerShip ship : state.entityData.playerShips) {
			if (!ship.recalled) {
				if (ship.core.currentCharge > 0f) {
					return false;
				} else {
					if (shipIsRecallable(state, ship)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean shipIsRecallable(MiningGameState state, MiningPlayerShip ship) {
		return MiningPlayerShipProcessor.shipIsInRecallArea(state, ship) 
				&& ship.speed <= 0.1f && ship.speed >= -0.1f 
				&& state.metaData.pauseData.resumeCountdownMS <= 0;
	}

	public void updateGameState(MiningGameState state, long deltaTimeMS) {
		engine.update(state, deltaTimeMS);
	}
	
}
