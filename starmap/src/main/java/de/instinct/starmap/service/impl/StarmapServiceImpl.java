package de.instinct.starmap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import base.file.FileManager;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.CompletionResponse;
import de.instinct.api.starmap.dto.GalaxyData;
import de.instinct.api.starmap.dto.PlayerStarmapData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;
import de.instinct.api.starmap.dto.StarsystemData;
import de.instinct.api.starmap.dto.SystemCompletionData;
import de.instinct.starmap.service.StarmapService;

@Service
public class StarmapServiceImpl implements StarmapService {
	
	private Map<String, PlayerStarmapData> starmaps;
	private SectorData baseSectorData;
	
	public StarmapServiceImpl() {
		starmaps = new HashMap<>();
	}

	@Override
	public StarmapInitializationResponseCode init(String token) {
		if (starmaps.get(token) != null) return StarmapInitializationResponseCode.ALREADY_INITIALIZED;
		List<GalaxyData> initGalaxies = new ArrayList<>();
		
		getSectorData();
		initGalaxies.add(GalaxyData.builder()
				.id(baseSectorData.getGalaxies().get(0).getId())
				.mapPosX(baseSectorData.getGalaxies().get(0).getMapPosX())
				.mapPosY(baseSectorData.getGalaxies().get(0).getMapPosY())
				.name(baseSectorData.getGalaxies().get(0).getName())
				.starsystems(new ArrayList<>())
				.build());
		initGalaxies.get(0).getStarsystems().add(baseSectorData.getGalaxies().get(0).getStarsystems().get(0));
		
		starmaps.put(token, PlayerStarmapData.builder()
				.completedSystems(new ArrayList<>())
				.sectorData(SectorData.builder()
						.galaxies(initGalaxies)
						.build())
				.build());
		return StarmapInitializationResponseCode.SUCCESS;
	}

	@Override
	public PlayerStarmapData getStarmapData(String token) {
		PlayerStarmapData starmapData = starmaps.get(token);
		if (starmapData == null) {
			init(token);
			starmapData = starmaps.get(token);
		}
		return starmapData;
	}
	
	@Override
	public SectorData getSectorData() {
		if (baseSectorData == null) baseSectorData = ObjectJSONMapper.mapJSON(FileManager.loadFile("init.data"), SectorData.class);;
		return baseSectorData;
	}

	@Override
	public CompletionResponse completeSystem(CompletionRequest request) {
		PlayerStarmapData starmapData = starmaps.get(request.getUserUUID());
		if (starmapData == null) return CompletionResponse.USERUUID_DOESNT_EXIST;
		SystemCompletionData systemCompletionData = null;
		for (SystemCompletionData data : starmapData.getCompletedSystems()) {
			if (data.getGalaxyId() == request.getGalaxyId() && data.getSystemId() == request.getSystemId()) {
				systemCompletionData = data;
				break;
			}
		}
		if (systemCompletionData != null) {
			systemCompletionData.setLastCompletedAt(System.currentTimeMillis());
			systemCompletionData.setCompletedTimes(systemCompletionData.getCompletedTimes() + 1);
		} else {
			starmapData.getCompletedSystems().add(SystemCompletionData.builder()
					.galaxyId(request.getGalaxyId())
					.systemId(request.getSystemId())
					.lastCompletedAt(System.currentTimeMillis())
					.completedTimes(1)
					.firstCompletedAt(System.currentTimeMillis())
					.build());
			unlockNextSystem(starmapData);
		}
		
		return CompletionResponse.SUCCESS;
	}

	private void unlockNextSystem(PlayerStarmapData playerData) {
	    GalaxyData playerLastGalaxy = playerData.getSectorData().getGalaxies().get(playerData.getSectorData().getGalaxies().size() - 1);
	    StarsystemData playerLastSystem = playerLastGalaxy.getStarsystems().get(playerLastGalaxy.getStarsystems().size() - 1);
	    
	    GalaxyData baseGalaxy = getSectorData().getGalaxies().stream()
	            .filter(g -> g.getId() == playerLastGalaxy.getId())
	            .findFirst()
	            .orElse(null);
	    if (baseGalaxy == null) return;
	    StarsystemData lastSystemInBaseGalaxy = baseGalaxy.getStarsystems().get(baseGalaxy.getStarsystems().size() - 1);
	    boolean isLastSystemInGalaxy = (playerLastSystem.getId() == lastSystemInBaseGalaxy.getId());
	    if (!isLastSystemInGalaxy) {
	        baseGalaxy.getStarsystems().stream()
	                .filter(s -> s.getId() == playerLastSystem.getId() + 1)
	                .findFirst()
	                .ifPresent(playerLastGalaxy.getStarsystems()::add);
	    } else {
	        List<GalaxyData> baseGalaxies = getSectorData().getGalaxies();
	        int currentGalaxyIndex = -1;
	        
	        for (int i = 0; i < baseGalaxies.size(); i++) {
	            if (baseGalaxies.get(i).getId() == playerLastGalaxy.getId()) {
	                currentGalaxyIndex = i;
	                break;
	            }
	        }
	        
	        if (currentGalaxyIndex >= 0 && currentGalaxyIndex + 1 < baseGalaxies.size()) {
	            GalaxyData nextBaseGalaxy = baseGalaxies.get(currentGalaxyIndex + 1);
	            
	            GalaxyData newPlayerGalaxy = GalaxyData.builder()
	                    .id(nextBaseGalaxy.getId())
	                    .mapPosX(nextBaseGalaxy.getMapPosX())
	                    .mapPosY(nextBaseGalaxy.getMapPosY())
	                    .name(nextBaseGalaxy.getName())
	                    .starsystems(new ArrayList<>())
	                    .build();
	            
	            if (!nextBaseGalaxy.getStarsystems().isEmpty()) {
	                newPlayerGalaxy.getStarsystems().add(nextBaseGalaxy.getStarsystems().get(0));
	            }
	            
	            playerData.getSectorData().getGalaxies().add(newPlayerGalaxy);
	        }
	    }
	}


}
