package de.instinct.starmap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import base.file.FileManager;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.starmap.dto.CompletionRequest;
import de.instinct.api.starmap.dto.CompletionResponse;
import de.instinct.api.starmap.dto.PlayerStarmapData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;
import de.instinct.api.starmap.dto.SystemCompletionData;
import de.instinct.starmap.service.StarmapService;

@Service
public class StarmapServiceImpl implements StarmapService {
	
	private Map<String, PlayerStarmapData> starmaps;
	private SectorData sectorData;
	
	public StarmapServiceImpl() {
		starmaps = new HashMap<>();
	}

	@Override
	public StarmapInitializationResponseCode init(String token) {
		if (starmaps.get(token) != null) return StarmapInitializationResponseCode.ALREADY_INITIALIZED;
		starmaps.put(token, PlayerStarmapData.builder()
				.completedSystems(new ArrayList<>())
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
		if (sectorData == null) sectorData = ObjectJSONMapper.mapJSON(FileManager.loadFile("init.data"), SectorData.class);
		return sectorData;
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
		}
		
		return CompletionResponse.SUCCESS;
	}

}
