package de.instinct.starmap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
	public SectorData sector(FactionMode mode) {
		if (baseSectorData == null) load();
		return baseSectorData.get(mode);
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
