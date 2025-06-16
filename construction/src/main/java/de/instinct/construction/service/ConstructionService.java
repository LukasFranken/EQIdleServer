package de.instinct.construction.service;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;
import de.instinct.api.construction.dto.UseTurretResponseCode;

public interface ConstructionService {

	InfrastructureInitializationResponseCode init(String token);

	Infrastructure getInfrastructure(String token);

	UseTurretResponseCode useTurret(String token, String turretUUID);

}
