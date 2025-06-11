package de.instinct.construction.service;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;

public interface ConstructionService {

	InfrastructureInitializationResponseCode init(String token);

	Infrastructure getInfrastructure(String token);

}
