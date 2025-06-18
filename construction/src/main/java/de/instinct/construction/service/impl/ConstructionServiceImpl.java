package de.instinct.construction.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;
import de.instinct.api.construction.dto.PlanetDefense;
import de.instinct.api.construction.dto.PlanetTurretBlueprint;
import de.instinct.api.construction.dto.PlanetWeapon;
import de.instinct.api.construction.dto.UseTurretResponseCode;
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
				.planetTurretBlueprints(getDefaultPlanetTurretBlueprints())
				.build());
		return InfrastructureInitializationResponseCode.SUCCESS;
	}

	private List<PlanetTurretBlueprint> getDefaultPlanetTurretBlueprints() {
		List<PlanetTurretBlueprint> blueprints = new ArrayList<>();
		blueprints.add(PlanetTurretBlueprint.builder()
				.uuid(UUID.randomUUID().toString())
				.name("Projectile Turret")
				.planetDefense(PlanetDefense.builder()
						.shield(15)
						.armor(15)
						.shieldRegenerationSpeed(0.5f)
						.build())
				.planetWeapon(PlanetWeapon.builder()
						.type(WeaponType.PROJECTILE)
						.damage(3)
						.range(80f)
						.speed(120f)
						.cooldown(500)
						.build())
				.inUse(true)
				.build());
		
		blueprints.add(PlanetTurretBlueprint.builder()
				.uuid(UUID.randomUUID().toString())
				.name("Laser Turret")
				.planetDefense(PlanetDefense.builder()
						.shield(15)
						.armor(15)
						.shieldRegenerationSpeed(0.5f)
						.build())
				.planetWeapon(PlanetWeapon.builder()
						.type(WeaponType.LASER)
						.damage(5)
						.range(100f)
						.speed(120f)
						.cooldown(1000)
						.build())
				.inUse(false)
				.build());
		
		blueprints.add(PlanetTurretBlueprint.builder()
				.uuid(UUID.randomUUID().toString())
				.name("Missile Turret")
				.planetDefense(PlanetDefense.builder()
						.shield(10)
						.armor(10)
						.shieldRegenerationSpeed(0.5f)
						.build())
				.planetWeapon(PlanetWeapon.builder()
						.type(WeaponType.MISSILE)
						.damage(11)
						.range(120f)
						.speed(60f)
						.cooldown(3000)
						.build())
				.inUse(false)
				.build());
		
		blueprints.add(PlanetTurretBlueprint.builder()
				.uuid(UUID.randomUUID().toString())
				.name("Beam Turret")
				.planetDefense(PlanetDefense.builder()
						.shield(10)
						.armor(10)
						.shieldRegenerationSpeed(0.5f)
						.build())
				.planetWeapon(PlanetWeapon.builder()
						.type(WeaponType.BEAM)
						.damage(10)
						.range(130f)
						.speed(220f)
						.cooldown(3000)
						.build())
				.inUse(false)
				.build());
		return blueprints;
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

	@Override
	public UseTurretResponseCode useTurret(String token, String turretUUID) {
		Infrastructure infrastructure = userInfrastructures.get(token);
		if (infrastructure == null) return UseTurretResponseCode.NOT_INITIALIZED;
		PlanetTurretBlueprint blueprint = infrastructure.getPlanetTurretBlueprints().stream()
				.filter(s -> s.getUuid().equals(turretUUID))
				.findFirst()
				.orElse(null);
		if (blueprint == null) return UseTurretResponseCode.INVALID_UUID;
		if (blueprint.isInUse()) return UseTurretResponseCode.ALREADY_IN_USE;
		for (PlanetTurretBlueprint b : infrastructure.getPlanetTurretBlueprints()) {
			b.setInUse(false);
		}
		blueprint.setInUse(true);
		return UseTurretResponseCode.SUCCESS;
	}
	
	

}
