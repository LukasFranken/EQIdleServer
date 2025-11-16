package de.instinct.api.shipyard.service.impl;

import java.util.ArrayList;
import java.util.List;

import de.instinct.api.shipyard.dto.ship.ShipBlueprint;
import de.instinct.api.shipyard.dto.ship.ShipComponent;
import de.instinct.api.shipyard.dto.ship.ShipCore;
import de.instinct.api.shipyard.dto.ship.ShipEngine;
import de.instinct.api.shipyard.dto.ship.ShipHull;
import de.instinct.api.shipyard.dto.ship.ShipShield;
import de.instinct.api.shipyard.dto.ship.ShipWeapon;
import de.instinct.api.shipyard.service.model.ShipComponentType;

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

}
