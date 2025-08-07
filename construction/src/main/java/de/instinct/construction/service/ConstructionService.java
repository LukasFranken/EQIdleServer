package de.instinct.construction.service;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;
import de.instinct.api.construction.dto.PlayerInfrastructure;
import de.instinct.api.construction.dto.UseTurretResponseCode;

public interface ConstructionService {

	InfrastructureInitializationResponseCode init(String token);

	PlayerInfrastructure getInfrastructure(String token);
	
	Infrastructure getBaseData();

	UseTurretResponseCode useTurret(String token, String turretUUID);

}
