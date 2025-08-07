package de.instinct.construction.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.instinct.api.construction.dto.Infrastructure;
import de.instinct.api.construction.dto.InfrastructureInitializationResponseCode;
import de.instinct.api.construction.dto.PlanetDefense;
import de.instinct.api.construction.dto.PlanetTurretBlueprint;
import de.instinct.api.construction.dto.PlanetWeapon;
import de.instinct.api.construction.dto.PlayerInfrastructure;
import de.instinct.api.construction.dto.PlayerTurretData;
import de.instinct.api.construction.dto.UseTurretResponseCode;
import de.instinct.api.construction.dto.WeaponType;
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
		initInfrastructure.setMaxResourceCapacity(baseInfrastructure.getBaseMaxResourceCapacity());
		initInfrastructure.setResourceGenerationSpeed(baseInfrastructure.getBaseResourceGenerationSpeed());
		initInfrastructure.setPlayerTurrets(new ArrayList<>());
		PlayerTurretData testPlayerTurretData = new PlayerTurretData();
		testPlayerTurretData.setUuid("test-turret-uuid");
		testPlayerTurretData.setTurretId(0);
		initInfrastructure.getPlayerTurrets().add(testPlayerTurretData);
		playerInfrastructures.put(token, initInfrastructure);
		return InfrastructureInitializationResponseCode.SUCCESS;
	}

	private Infrastructure getBaseInfrastructure() {
		Infrastructure infrastructure = new Infrastructure();
		infrastructure.setBaseMaxResourceCapacity(10);
		infrastructure.setBaseResourceGenerationSpeed(1f);
		List<PlanetTurretBlueprint> blueprints = new ArrayList<>();
		PlanetTurretBlueprint projectileTurretBlueprint = new PlanetTurretBlueprint();
		projectileTurretBlueprint.setName("Projectile");
		projectileTurretBlueprint.setRotationSpeed(1f);
		projectileTurretBlueprint.setId(0);
		projectileTurretBlueprint.setCost(10);
		projectileTurretBlueprint.setCommandPointsCost(3);
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
		blueprints.add(projectileTurretBlueprint);
		
		PlanetTurretBlueprint laserTurretBlueprint = new PlanetTurretBlueprint();
		laserTurretBlueprint.setName("Laser");
		laserTurretBlueprint.setRotationSpeed(1f);
		laserTurretBlueprint.setId(1);
		laserTurretBlueprint.setCost(10);
		laserTurretBlueprint.setCommandPointsCost(3);
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
		blueprints.add(laserTurretBlueprint);

		PlanetTurretBlueprint missileTurretBlueprint = new PlanetTurretBlueprint();
		missileTurretBlueprint.setName("Missile");
		missileTurretBlueprint.setRotationSpeed(1f);
		missileTurretBlueprint.setId(2);
		missileTurretBlueprint.setCost(10);
		missileTurretBlueprint.setCommandPointsCost(3);
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
		blueprints.add(missileTurretBlueprint);
		
		PlanetTurretBlueprint beamTurretBlueprint = new PlanetTurretBlueprint();
		beamTurretBlueprint.setName("Beam");
		beamTurretBlueprint.setRotationSpeed(1f);
		beamTurretBlueprint.setId(3);
		beamTurretBlueprint.setCost(10);
		beamTurretBlueprint.setCommandPointsCost(3);
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
		blueprints.add(beamTurretBlueprint);
		infrastructure.setTurretBlueprints(blueprints);
		return infrastructure;
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
			//baseInfrastructure = ObjectJSONMapper.mapJSON(FileManager.loadFile("init.data"), Infrastructure.class);
			baseInfrastructure = getBaseInfrastructure();
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
