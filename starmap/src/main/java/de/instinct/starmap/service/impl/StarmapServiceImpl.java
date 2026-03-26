package de.instinct.starmap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.instinct.api.core.API;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.matchmaking.dto.GroupLobbyCreationRequest;
import de.instinct.api.matchmaking.dto.LobbyCreationResponse;
import de.instinct.api.matchmaking.dto.LobbyTypeSetResponse;
import de.instinct.api.matchmaking.model.FactionMode;
import de.instinct.api.matchmaking.model.GameMode;
import de.instinct.api.matchmaking.model.GameType;
import de.instinct.api.matchmaking.model.VersusMode;
import de.instinct.api.social.dto.Group;
import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.CompletionResponse;
import de.instinct.api.starmap.dto.GalaxyData;
import de.instinct.api.starmap.dto.PlayerStarmapData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.SectorDataRequest;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;
import de.instinct.api.starmap.dto.StarsystemData;
import de.instinct.api.starmap.dto.StartConquestRequest;
import de.instinct.api.starmap.dto.StartConquestResponse;
import de.instinct.api.starmap.dto.SystemCompletionData;
import de.instinct.api.starmap.service.StarmapInterface;
import de.instinct.base.file.FileManager;

@Service
public class StarmapServiceImpl implements StarmapInterface {
	
	private Map<String, PlayerStarmapData> starmaps;
	private Map<FactionMode, SectorData> baseSectorData;
	
	public StarmapServiceImpl() {
		starmaps = new HashMap<>();
	}

	@Override
	public StarmapInitializationResponseCode init(String token) {
		if (starmaps.get(token) != null) return StarmapInitializationResponseCode.ALREADY_INITIALIZED;
		PlayerStarmapData initStarmap = new PlayerStarmapData();
		initStarmap.setCompletedSystems(new HashMap<>());
		initStarmap.getCompletedSystems().put(FactionMode.ONE_VS_ONE, new ArrayList<>());
		initStarmap.getCompletedSystems().put(FactionMode.TWO_VS_TWO, new ArrayList<>());
		initStarmap.getCompletedSystems().put(FactionMode.THREE_VS_THREE, new ArrayList<>());
		starmaps.put(token, initStarmap);
		return StarmapInitializationResponseCode.SUCCESS;
	}

	@Override
	public PlayerStarmapData data(String token) {
		PlayerStarmapData starmapData = starmaps.get(token);
		if (starmapData == null) {
			init(token);
			starmapData = starmaps.get(token);
		}
		return starmapData;
	}
	
	@Override
	public String load() {
		baseSectorData = new HashMap<>();
		for (FactionMode mode : FactionMode.values()) {
			SectorData sectorData = ObjectJSONMapper.mapJSON(FileManager.loadFile("conquests/" + mode.toString().toLowerCase() + ".data"), SectorData.class);
			baseSectorData.put(mode, sectorData);
			for (GalaxyData galaxy : sectorData.getGalaxies()) {
				for (StarsystemData system : galaxy.getStarsystems()) {
					system.setMapPreview(API.game().preview(mode, galaxy.getId() + "_" + system.getId()));
				}
			}
		}
		return "OK";
	}
	
	@Override
	public SectorData sector(SectorDataRequest request) {
		if (baseSectorData == null) load();
		switch (request.getType()) {
		case FULL:
			if (request.getMode() != null) return baseSectorData.get(request.getMode());
			break;
		case GROUP:
			if (request.getGroupToken() != null) {
				Group group = API.social().getGroup(request.getGroupToken());
				if (group != null) {
					List<String> playerUUIDs = new ArrayList<>();
					for (String memberName : group.getMembers()) {
						playerUUIDs.add(API.meta().token(memberName));
					}
					return calculateSectorData(playerUUIDs);
				}
			}
			break;
		case SOLO:
			if (request.getToken() != null) return calculateSectorData(List.of(request.getToken()));
			break;
		}
		return null;
	}

	private SectorData calculateSectorData(List<String> playerUUIDs) {
		int globallyFurthestGalaxyId = 0;
		int globallyFurthestSystemId = -1;
		for (String playerUUID : playerUUIDs) {
			PlayerStarmapData starmapData = starmaps.get(playerUUID);
			if (starmapData != null) {
				List<SystemCompletionData> systemCompletionData = starmapData.getCompletedSystems().get(FactionMode.fromTeamPlayerCount(playerUUIDs.size()));
				if (systemCompletionData != null) {
					if (systemCompletionData.size() == 0) {
						globallyFurthestGalaxyId = 0;
						globallyFurthestSystemId = -1;
						break;
					}
					SystemCompletionData lastSystemCompletionData = systemCompletionData.get(systemCompletionData.size() - 1);
					if (globallyFurthestSystemId == -1) {
						globallyFurthestGalaxyId = lastSystemCompletionData.getGalaxyId();
						globallyFurthestSystemId = lastSystemCompletionData.getSystemId();
						continue;
					}
					if (lastSystemCompletionData.getGalaxyId() < globallyFurthestGalaxyId) {
						globallyFurthestGalaxyId = lastSystemCompletionData.getGalaxyId();
						globallyFurthestSystemId = lastSystemCompletionData.getSystemId();
					}
					if (lastSystemCompletionData.getGalaxyId() == globallyFurthestGalaxyId && lastSystemCompletionData.getSystemId() < globallyFurthestSystemId) {
						globallyFurthestSystemId = lastSystemCompletionData.getSystemId();
					}
				}
			}
		}
		
		SectorData sectorData = new SectorData();
		sectorData.setGalaxies(new ArrayList<>());
		boolean wonLastInGalaxy = true;
		for (GalaxyData galaxyData : baseSectorData.get(FactionMode.fromTeamPlayerCount(playerUUIDs.size())).getGalaxies()) {
			if (galaxyData.getId() < globallyFurthestGalaxyId) {
				sectorData.getGalaxies().add(galaxyData);
				continue;
			}
			if (galaxyData.getId() == globallyFurthestGalaxyId) {
				GalaxyData newGalaxyData = new GalaxyData();
				newGalaxyData.setId(galaxyData.getId());
				newGalaxyData.setName(galaxyData.getName());
				newGalaxyData.setMapPosX(galaxyData.getMapPosX());
				newGalaxyData.setMapPosY(galaxyData.getMapPosY());
				newGalaxyData.setStarsystems(new ArrayList<>());
				for (StarsystemData systemData : galaxyData.getStarsystems()) {
					if (systemData.getId() <= globallyFurthestSystemId) {
						newGalaxyData.getStarsystems().add(systemData);
					} else {
						if (systemData.getId() <= globallyFurthestSystemId + 1) {
							newGalaxyData.getStarsystems().add(systemData);
						}
						wonLastInGalaxy = false;
						break;
					}
				}
				sectorData.getGalaxies().add(newGalaxyData);
			}
			if (galaxyData.getId() > globallyFurthestGalaxyId && wonLastInGalaxy) {
				GalaxyData newGalaxyData = new GalaxyData();
				newGalaxyData.setId(galaxyData.getId());
				newGalaxyData.setName(galaxyData.getName());
				newGalaxyData.setMapPosX(galaxyData.getMapPosX());
				newGalaxyData.setMapPosY(galaxyData.getMapPosY());
				newGalaxyData.setStarsystems(new ArrayList<>());
				newGalaxyData.getStarsystems().add(galaxyData.getStarsystems().get(0));
				sectorData.getGalaxies().add(newGalaxyData);
				wonLastInGalaxy = false;
			}
		}
		return sectorData;
	}

	@Override
	public CompletionResponse complete(CompletionRequest request) {
		PlayerStarmapData starmapData = starmaps.get(request.getUserUUID());
		if (starmapData == null) return CompletionResponse.USERUUID_DOESNT_EXIST;
		SystemCompletionData systemCompletionData = null;
		for (SystemCompletionData data : starmapData.getCompletedSystems().get(request.getMode())) {
			if (data.getGalaxyId() == request.getGalaxyId() && data.getSystemId() == request.getSystemId()) {
				systemCompletionData = data;
				break;
			}
		}
		if (systemCompletionData != null) {
			systemCompletionData.setLastCompletedAt(System.currentTimeMillis());
			systemCompletionData.setCompletedTimes(systemCompletionData.getCompletedTimes() + 1);
		} else {
			systemCompletionData = new SystemCompletionData();
			systemCompletionData.setGalaxyId(request.getGalaxyId());
			systemCompletionData.setSystemId(request.getSystemId());
			systemCompletionData.setLastCompletedAt(System.currentTimeMillis());
			systemCompletionData.setCompletedTimes(1);
			systemCompletionData.setFirstCompletedAt(System.currentTimeMillis());
			
			starmapData.getCompletedSystems().get(request.getMode()).add(systemCompletionData);
		}
		
		return CompletionResponse.SUCCESS;
	}

	@Override
	public StartConquestResponse start(StartConquestRequest request) {
		GroupLobbyCreationRequest groupLobbyCreationRequest = new GroupLobbyCreationRequest();
		groupLobbyCreationRequest.setPlayerUUIDs(new ArrayList<>());
		if (request.getGroupToken() == null) {
			if (request.getUserToken() == null) return StartConquestResponse.USERTOKEN_MISSING;
			groupLobbyCreationRequest.getPlayerUUIDs().add(request.getUserToken());
		} else {
			Group group = API.social().getGroup(request.getGroupToken());
			if (group == null) return StartConquestResponse.GROUP_DOESNT_EXIST;
			for (String memberName : group.getMembers()) {
				groupLobbyCreationRequest.getPlayerUUIDs().add(API.meta().token(memberName));
			}
		}
		LobbyCreationResponse lobbyCreationResponse = API.matchmaking().creategrouplobby(groupLobbyCreationRequest);
		LobbyTypeSetResponse lobbyTypeSetResponse = API.matchmaking().settype(lobbyCreationResponse.getLobbyUUID(), 
	    		GameType.builder()
	    		.map(request.getGalaxyId() + "_" + request.getSystemId())
	    		.factionMode(FactionMode.fromTeamPlayerCount(groupLobbyCreationRequest.getPlayerUUIDs().size()))
	    		.gameMode(GameMode.CONQUEST)
	    		.versusMode(VersusMode.AI)
	    		.build());
		if (lobbyTypeSetResponse == LobbyTypeSetResponse.SUCCESS) {
			API.matchmaking().start(lobbyCreationResponse.getLobbyUUID());
			return StartConquestResponse.SUCCESS;
		} else {
			return StartConquestResponse.ERROR;
		}
	}

}
