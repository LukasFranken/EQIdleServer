package de.instinct.api.construction.service;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;
import de.instinct.api.construction.dto.PlayerInfrastructure;
import de.instinct.api.construction.dto.UseTurretResponseCode;
import de.instinct.api.core.service.BaseServiceInterface;

public interface ConstructionInterface extends BaseServiceInterface {
	
	InfrastructureInitializationResponseCode init(String token);
	
	PlayerInfrastructure data(String token);
	
	Infrastructure construction();
	
	UseTurretResponseCode use(String token, String turretUUID);

}
