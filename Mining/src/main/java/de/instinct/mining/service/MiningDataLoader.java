package de.instinct.mining.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.mining.dto.player.MiningPlayerShipData;
import de.instinct.base.file.FileManager;
import de.instinct.engine.mining.data.MiningGameState;
import de.instinct.engine.mining.data.map.MiningMap;
import de.instinct.engine.mining.data.map.node.types.AsteroidMapNode;
import de.instinct.engine.mining.data.map.node.types.RecallAreaNode;
import de.instinct.engine.mining.player.MiningPlayer;
import de.instinct.engine_api.mining.MiningStateManager;
import de.instinct.engine_api.mining.model.MiningGameStateInitialization;
import de.instinct.mining.service.model.AsteroidMapData;
import de.instinct.mining.service.model.MiningClient;
import de.instinct.mining.service.model.MiningMapData;

public class MiningDataLoader {
	
	private static final String MAP_FILE_SUBFOLDER = "maps";
	private static final String MAP_FILE_POSTFIX = ".map";
	
	private MiningStateManager stateManager;
	
	public MiningDataLoader() {
		stateManager = new MiningStateManager();
	}
	
	public MiningGameState loadState(String map, List<MiningClient> clients) {
		MiningGameStateInitialization initialization = new MiningGameStateInitialization();
		initialization.setGameUUID(UUID.randomUUID().toString());
		initialization.setPlayers(new ArrayList<>());
		initialization.setMap(loadMap(map));
		for (MiningClient client : clients) {
			MiningPlayer player = stateManager.getPlayer(client.getPlayerData());
			player.id = client.getPlayerId();
			initialization.getPlayers().add(player);
		}
		initialization.setPauseCountLimit(3);
		initialization.setPauseTimeLimitMS(60_000);
		return stateManager.initializeMining(initialization);
	}
	
	private MiningMap loadMap(String mapName) {
		MiningMapData mapData = ObjectJSONMapper.mapJSON(FileManager.loadFile(MAP_FILE_SUBFOLDER + "/" + mapName + MAP_FILE_POSTFIX), MiningMapData.class);
		MiningMap map = new MiningMap();
		map.nodes = new ArrayList<>();
		RecallAreaNode recallAreaNode = new RecallAreaNode();
		recallAreaNode.position = mapData.getRecallPosition();
		recallAreaNode.radius = mapData.getRecallRadius();
		map.nodes.add(recallAreaNode);
		for (AsteroidMapData asteroidData : mapData.getAsteroids()) {
			AsteroidMapNode asteroidNode = new AsteroidMapNode();
			asteroidNode.position = asteroidData.getPosition();
			asteroidNode.radius = asteroidData.getRadius();
			asteroidNode.health = asteroidData.getHealth();
			asteroidNode.resourceType = asteroidData.getResourceType();
			asteroidNode.value = asteroidData.getValue();
			map.nodes.add(asteroidNode);
		}
		return map;
	}
	
	public MiningPlayerShipData loadBaseShipData() {
		return ObjectJSONMapper.mapJSON(FileManager.loadFile("ship.data"), MiningPlayerShipData.class);
	}

}
