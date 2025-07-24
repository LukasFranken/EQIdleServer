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
		Infrastructure initInfrastructure = new Infrastructure();
		initInfrastructure.setMaxResourceCapacity(20f);
		initInfrastructure.setPercentOfArmorAfterCapture(0.2f);
		initInfrastructure.setResourceGenerationSpeed(1f);
		initInfrastructure.setPlanetTurretBlueprints(getDefaultPlanetTurretBlueprints());
		userInfrastructures.put(token, initInfrastructure);
		return InfrastructureInitializationResponseCode.SUCCESS;
	}

	private List<PlanetTurretBlueprint> getDefaultPlanetTurretBlueprints() {
		List<PlanetTurretBlueprint> blueprints = new ArrayList<>();
		PlanetTurretBlueprint projectileTurretBlueprint = new PlanetTurretBlueprint();
		projectileTurretBlueprint.setUuid(UUID.randomUUID().toString());
		projectileTurretBlueprint.setName("Projectile");
		PlanetDefense projectileDefense = new PlanetDefense();
		projectileDefense.setShield(15);
		projectileDefense.setArmor(15);
		projectileDefense.setShieldRegenerationSpeed(0.5f);
		projectileTurretBlueprint.setPlanetDefense(projectileDefense);
		PlanetWeapon projectileWeapon = new PlanetWeapon();
		projectileWeapon.setType(WeaponType.PROJECTILE);
		projectileWeapon.setDamage(3);
		projectileWeapon.setRange(80f);
		projectileWeapon.setSpeed(120f);
		projectileWeapon.setCooldown(500);
		projectileTurretBlueprint.setPlanetWeapon(projectileWeapon);
		projectileTurretBlueprint.setInUse(true);
		blueprints.add(projectileTurretBlueprint);
		
		PlanetTurretBlueprint laserTurretBlueprint = new PlanetTurretBlueprint();
		laserTurretBlueprint.setUuid(UUID.randomUUID().toString());
		laserTurretBlueprint.setName("Laser");
		PlanetDefense laserDefense = new PlanetDefense();
		laserDefense.setShield(15);
		laserDefense.setArmor(15);
		laserDefense.setShieldRegenerationSpeed(0.5f);
		laserTurretBlueprint.setPlanetDefense(laserDefense);
		PlanetWeapon laserWeapon = new PlanetWeapon();
		laserWeapon.setType(WeaponType.LASER);
		laserWeapon.setDamage(5);
		laserWeapon.setRange(100f);
		laserWeapon.setSpeed(120f);
		laserWeapon.setCooldown(1000);
		laserTurretBlueprint.setPlanetWeapon(laserWeapon);
		laserTurretBlueprint.setInUse(false);
		blueprints.add(laserTurretBlueprint);

		PlanetTurretBlueprint missileTurretBlueprint = new PlanetTurretBlueprint();
		missileTurretBlueprint.setUuid(UUID.randomUUID().toString());
		missileTurretBlueprint.setName("Missile");
		PlanetDefense missileDefense = new PlanetDefense();
		missileDefense.setShield(10);
		missileDefense.setArmor(10);
		missileDefense.setShieldRegenerationSpeed(0.5f);
		missileTurretBlueprint.setPlanetDefense(missileDefense);
		PlanetWeapon missileWeapon = new PlanetWeapon();
		missileWeapon.setType(WeaponType.MISSILE);
		missileWeapon.setDamage(11);
		missileWeapon.setRange(120f);
		missileWeapon.setSpeed(60f);
		missileWeapon.setCooldown(3000);
		missileTurretBlueprint.setPlanetWeapon(missileWeapon);
		missileTurretBlueprint.setInUse(false);
		blueprints.add(missileTurretBlueprint);
		
		PlanetTurretBlueprint beamTurretBlueprint = new PlanetTurretBlueprint();
		beamTurretBlueprint.setUuid(UUID.randomUUID().toString());
		beamTurretBlueprint.setName("Beam");
		PlanetDefense beamDefense = new PlanetDefense();
		beamDefense.setShield(10);
		beamDefense.setArmor(10);
		beamDefense.setShieldRegenerationSpeed(0.5f);
		beamTurretBlueprint.setPlanetDefense(beamDefense);
		PlanetWeapon beamWeapon = new PlanetWeapon();
		beamWeapon.setType(WeaponType.BEAM);
		beamWeapon.setDamage(10);
		beamWeapon.setRange(130f);
		beamWeapon.setSpeed(220f);
		beamWeapon.setCooldown(3000);
		beamTurretBlueprint.setPlanetWeapon(beamWeapon);
		beamTurretBlueprint.setInUse(false);
		blueprints.add(beamTurretBlueprint);
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
