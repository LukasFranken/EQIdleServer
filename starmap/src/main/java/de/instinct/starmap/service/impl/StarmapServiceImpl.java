package de.instinct.starmap.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import base.file.FileManager;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;
import de.instinct.starmap.service.StarmapService;

@Service
public class StarmapServiceImpl implements StarmapService {
	
	private Map<String, SectorData> starmaps;
	
	public StarmapServiceImpl() {
		starmaps = new HashMap<>();
	}

	@Override
	public StarmapInitializationResponseCode init(String token) {
		if (starmaps.get(token) != null) return StarmapInitializationResponseCode.ALREADY_INITIALIZED;
		starmaps.put(token, ObjectJSONMapper.mapJSON(FileManager.loadFile("init.data"), SectorData.class));
		return StarmapInitializationResponseCode.SUCCESS;
	}

	@Override
	public SectorData getStarmapData(String token) {
		SectorData sector = starmaps.get(token);
		if (sector == null) {
			init(token);
			sector = starmaps.get(token);
		}
		return sector;
	}

}
