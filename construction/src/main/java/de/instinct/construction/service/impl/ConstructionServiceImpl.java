package de.instinct.construction.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;
import de.instinct.api.construction.dto.PlayerInfrastructure;
import de.instinct.api.construction.dto.PlayerTurretData;
import de.instinct.api.construction.dto.UseTurretResponseCode;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.base.file.FileManager;
import de.instinct.construction.service.ConstructionService;

@Service
public class ConstructionServiceImpl implements ConstructionService {
	
	private Map<String, PlayerInfrastructure> playerInfrastructures;
	private Infrastructure baseInfrastructure;
	
	public ConstructionServiceImpl() {
		playerInfrastructures = new HashMap<>();
	}

	@Override
	public InfrastructureInitializationResponseCode init(String token) {
		if (playerInfrastructures.containsKey(token)) return InfrastructureInitializationResponseCode.ALREADY_INITIALIZED;
		PlayerInfrastructure initInfrastructure = new PlayerInfrastructure();
		initInfrastructure.setPlanetResourceGenerationSpeed(getBaseData().getBasePlanetResourceGenerationSpeed());
		initInfrastructure.setTurretSlots(getBaseData().getBaseTurretSlots());
		initInfrastructure.setPlayerTurrets(new ArrayList<>());
		PlayerTurretData baseTurretData = new PlayerTurretData();
		baseTurretData.setUuid(UUID.randomUUID().toString());
		baseTurretData.setTurretId(getBaseData().getTurretBlueprints().get(0).getId());
		baseTurretData.setInUse(true);
		initInfrastructure.getPlayerTurrets().add(baseTurretData);
		playerInfrastructures.put(token, initInfrastructure);
		return InfrastructureInitializationResponseCode.SUCCESS;
	}

	@Override
	public PlayerInfrastructure getInfrastructure(String token) {
		PlayerInfrastructure playerInfrastructure = playerInfrastructures.get(token);
		if (playerInfrastructure == null) {
			init(token);
			playerInfrastructure = playerInfrastructures.get(token);
		}
		return playerInfrastructure;
	}
	
	@Override
	public Infrastructure getBaseData() {
		if (baseInfrastructure == null) {
			baseInfrastructure = ObjectJSONMapper.mapJSON(FileManager.loadFile("init.data"), Infrastructure.class);
		}
		return baseInfrastructure;
	}

	@Override
	public UseTurretResponseCode useTurret(String token, String turretUUID) {
		PlayerInfrastructure playerInfrastructure = playerInfrastructures.get(token);
		if (playerInfrastructure == null) return UseTurretResponseCode.NOT_INITIALIZED;
		PlayerTurretData playerTurretData = playerInfrastructure.getPlayerTurrets().stream()
				.filter(s -> s.getUuid().equals(turretUUID))
				.findFirst()
				.orElse(null);
		if (playerTurretData == null) return UseTurretResponseCode.INVALID_UUID;
		if (playerTurretData.isInUse()) return UseTurretResponseCode.ALREADY_IN_USE;
		for (PlayerTurretData unusePlayerTurretData : playerInfrastructure.getPlayerTurrets()) {
			unusePlayerTurretData.setInUse(false);
		}
		playerTurretData.setInUse(true);
		return UseTurretResponseCode.SUCCESS;
	}

}
