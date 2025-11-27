package de.instinct.api.shipyard.service.impl;

import java.util.ArrayList;
import java.util.List;

import de.instinct.api.shipyard.dto.admin.component.ComponentUpdateRequest;
import de.instinct.api.shipyard.dto.ship.ShipBlueprint;
import de.instinct.api.shipyard.dto.ship.ShipComponent;
import de.instinct.api.shipyard.dto.ship.ShipCore;
import de.instinct.api.shipyard.dto.ship.ShipEngine;
import de.instinct.api.shipyard.dto.ship.ShipHull;
import de.instinct.api.shipyard.dto.ship.ShipShield;
import de.instinct.api.shipyard.dto.ship.ShipWeapon;
import de.instinct.api.shipyard.dto.ship.component.ComponentAttribute;
import de.instinct.api.shipyard.dto.ship.component.ComponentLevel;
import de.instinct.api.shipyard.dto.ship.component.ShipComponentType;
import de.instinct.api.shipyard.dto.ship.component.attribute.CoreAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.EngineAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.HullAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.ShieldAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.WeaponAttribute;
import de.instinct.api.shipyard.dto.ship.component.level.CoreLevel;
import de.instinct.api.shipyard.dto.ship.component.level.EngineLevel;
import de.instinct.api.shipyard.dto.ship.component.level.HullLevel;
import de.instinct.api.shipyard.dto.ship.component.level.ShieldLevel;
import de.instinct.api.shipyard.dto.ship.component.level.WeaponLevel;
import de.instinct.api.shipyard.dto.ship.component.types.core.CoreAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.engine.EngineAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.hull.HullAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.shield.ShieldAttributeType;
import de.instinct.api.shipyard.dto.ship.component.types.weapon.WeaponAttributeType;
import de.instinct.engine.model.ship.components.types.CoreType;
import de.instinct.engine.model.ship.components.types.EngineType;
import de.instinct.engine.model.ship.components.types.HullType;
import de.instinct.engine.model.ship.components.types.ShieldType;
import de.instinct.engine.model.ship.components.types.WeaponType;

public class ShipyardUtility {
	
	public static List<ShipComponent> getShipComponentsByType(ShipBlueprint blueprint, ShipComponentType type) {
		List<ShipComponent> components = new ArrayList<>();
		for (ShipComponent component : blueprint.getComponents()) {
			switch (type) {
			case CORE:
				if (component instanceof ShipCore) components.add(component);
				break;
			case ENGINE:
				if (component instanceof ShipEngine) components.add(component);
				break;
			case HULL:
				if (component instanceof ShipHull) components.add(component);
				break;
			case SHIELD:
				if (component instanceof ShipShield) components.add(component);
				break;
			case WEAPON:
				if (component instanceof ShipWeapon) components.add(component);
				break;
			}
		}
		return components;
	}
	
	public static ShipComponent getShipComponentByType(ShipBlueprint blueprint, ShipComponentType type) {
		for (ShipComponent component : blueprint.getComponents()) {
			switch (type) {
			case CORE:
				if (component instanceof ShipCore) return component;
				break;
			case ENGINE:
				if (component instanceof ShipEngine) return component;
				break;
			case HULL:
				if (component instanceof ShipHull) return component;
				break;
			case SHIELD:
				if (component instanceof ShipShield) return component;
				break;
			case WEAPON:
				if (component instanceof ShipWeapon) return component;
				break;
			}
		}
		return null;
	}

	public static String getShipComponentType(ShipComponent component) {
		if (component instanceof ShipCore) return ShipComponentType.CORE.toString();
		if (component instanceof ShipEngine) return ShipComponentType.ENGINE.toString();
		if (component instanceof ShipHull) return ShipComponentType.HULL.toString();
		if (component instanceof ShipShield) return ShipComponentType.SHIELD.toString();
		if (component instanceof ShipWeapon) return ShipComponentType.WEAPON.toString();
		return null;
	}
	
	public static String getShipComponentSubtype(ShipComponent component) {
		if (component instanceof ShipCore) return ((ShipCore) component).getType().toString();
		if (component instanceof ShipEngine) return ((ShipEngine) component).getType().toString();
		if (component instanceof ShipHull) return ((ShipHull) component).getType().toString();
		if (component instanceof ShipShield) return ((ShipShield) component).getType().toString();
		if (component instanceof ShipWeapon) return ((ShipWeapon) component).getType().toString();
		return null;
	}

	public static String getComponentLevelType(ComponentLevel componentLevel) {
		if (componentLevel == null) return null;
		if (componentLevel instanceof CoreLevel) return ((CoreLevel)componentLevel).getRequirementType().toString();
		if (componentLevel instanceof EngineLevel) return ((EngineLevel)componentLevel).getRequirementType().toString();
		if (componentLevel instanceof HullLevel) return ((HullLevel)componentLevel).getRequirementType().toString();
		if (componentLevel instanceof ShieldLevel) return ((ShieldLevel)componentLevel).getRequirementType().toString();
		if (componentLevel instanceof WeaponLevel) return ((WeaponLevel)componentLevel).getRequirementType().toString();
		return null;
	}

	public static String getAttributeName(ComponentAttribute attribute) {
		if (attribute == null) return null;
		if (attribute instanceof CoreAttribute) return ((CoreAttribute)attribute).getType().toString();
		if (attribute instanceof EngineAttribute) return ((EngineAttribute)attribute).getType().toString();
		if (attribute instanceof HullAttribute) return ((HullAttribute)attribute).getType().toString();
		if (attribute instanceof ShieldAttribute) return ((ShieldAttribute)attribute).getType().toString();
		if (attribute instanceof WeaponAttribute) return ((WeaponAttribute)attribute).getType().toString();
		return null;
	}

	public static List<String> getAttributeOptions(ShipComponent component) {
		List<String> options = new ArrayList<>();
		if (component == null) return options;
		if (component instanceof ShipCore) {
			for (CoreAttributeType attribute : CoreAttributeType.values()) {
				options.add(attribute.toString());
			}
		}
		if (component instanceof ShipEngine) {
			for (EngineAttributeType attribute : EngineAttributeType.values()) {
				options.add(attribute.toString());
			}
		}
		if (component instanceof ShipHull) {
			for (HullAttributeType attribute : HullAttributeType.values()) {
				options.add(attribute.toString());
			}
		}
		if (component instanceof ShipShield) {
			for (ShieldAttributeType attribute : ShieldAttributeType.values()) {
				options.add(attribute.toString());
			}
		}
		if (component instanceof ShipWeapon) {
			for (WeaponAttributeType attribute : WeaponAttributeType.values()) {
				options.add(attribute.toString());
			}
		}
		return options;
	}

	public static void updateShipComponentType(ShipComponent component, ComponentUpdateRequest request) {
		if (component instanceof ShipCore) ((ShipCore) component).setType(CoreType.valueOf(request.getType()));
		if (component instanceof ShipEngine) ((ShipEngine) component).setType(EngineType.valueOf(request.getType()));
		if (component instanceof ShipHull) ((ShipHull) component).setType(HullType.valueOf(request.getType()));
		if (component instanceof ShipShield) ((ShipShield) component).setType(ShieldType.valueOf(request.getType()));
		if (component instanceof ShipWeapon) ((ShipWeapon) component).setType(WeaponType.valueOf(request.getType()));
	}

}
