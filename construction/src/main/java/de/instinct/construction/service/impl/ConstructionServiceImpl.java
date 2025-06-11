package de.instinct.construction.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;
import de.instinct.api.construction.dto.PlanetDefense;
import de.instinct.api.construction.dto.PlanetWeapon;
import de.instinct.api.construction.dto.WeaponType;
import de.instinct.construction.service.ConstructionService;

@Service
public class ConstructionServiceImpl implements ConstructionService {
	
private Map<String, Infrastructure> userInfrastructures;
	
	public ConstructionServiceImpl() {
		userInfrastructures = new HashMap<>();
	}

	@Override
	public InfrastructureInitializationResponseCode init(String token) {
		if (userInfrastructures.containsKey(token)) return InfrastructureInitializationResponseCode.ALREADY_INITIALIZED;
		userInfrastructures.put(token, Infrastructure.builder()
				.maxResourceCapacity(20f)
				.percentOfArmorAfterCapture(0.2f)
				.resourceGenerationSpeed(1f)
				.planetDefense(PlanetDefense.builder()
						.shield(10)
						.armor(10)
						.shieldRegenerationSpeed(0.5f)
						.build())
				.planetWeapon(PlanetWeapon.builder()
						.type(WeaponType.PROJECTILE)
						.damage(6)
						.range(100f)
						.speed(120f)
						.cooldown(2000)
						.build())
				.build());
		return InfrastructureInitializationResponseCode.SUCCESS;
	}

	@Override
	public Infrastructure getInfrastructure(String token) {
		Infrastructure infrastructure = userInfrastructures.get(token);
		if (infrastructure == null) {
			init(token);
			infrastructure = userInfrastructures.get(token);
		}
		return infrastructure;
	}
	
	

}
