package de.instinct.starmap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.instinct.api.meta.dto.Resource;
import de.instinct.api.meta.dto.ResourceAmount;
import de.instinct.api.starmap.dto.GalaxyData;
import de.instinct.api.starmap.dto.SectorData;
import de.instinct.api.starmap.dto.StarmapInitializationResponseCode;
import de.instinct.api.starmap.dto.StarsystemData;
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
		starmaps.put(token, SectorData.builder()
				.galaxies(loadInitialGalaxies())
				.build());
		return StarmapInitializationResponseCode.SUCCESS;
	}

	private List<GalaxyData> loadInitialGalaxies() {
		List<ResourceAmount> testResourceRewards = new ArrayList<>();
		testResourceRewards.add(ResourceAmount.builder()
				.type(Resource.CREDITS)
				.amount(500)
				.build());
		testResourceRewards.add(ResourceAmount.builder()
				.type(Resource.METAL)
				.amount(500)
				.build());
		testResourceRewards.add(ResourceAmount.builder()
				.type(Resource.CRYSTALS)
				.amount(500)
				.build());
		
		StarsystemData testStarSystem = StarsystemData.builder()
				.id(0)
				.name("Teststarsystem")
				.mapPosX(-20)
				.mapPosY(-20)
				.planets(6)
				.ancientPoints(50)
				.threatLevel(1)
				.conquered(false)
				.resourceRewards(testResourceRewards)
				.build();
		
		GalaxyData testGalaxy = GalaxyData.builder()
				.id(0)
				.name("Testgalaxy")
				.mapPosX(0)
				.mapPosY(0)
				.starsystems(new ArrayList<>())
				.build();
		testGalaxy.getStarsystems().add(testStarSystem);
		
		GalaxyData testGalaxy2 = GalaxyData.builder()
				.id(1)
				.name("Testgalaxy2")
				.mapPosX(20)
				.mapPosY(20)
				.starsystems(new ArrayList<>())
				.build();
		testGalaxy2.getStarsystems().add(testStarSystem);

		List<GalaxyData> galaxies = new ArrayList<>();
		galaxies.add(testGalaxy);
		galaxies.add(testGalaxy2);
		return galaxies;
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
