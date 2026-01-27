package de.instinct.shop.service.impl;

import java.util.ArrayList;
import java.util.List;

import de.instinct.api.shop.dto.item.ShopItemEffectData;
import de.instinct.shop.service.model.ShopItemEffect;
import de.instinct.shop.service.model.effects.ActiveShipSlotExpansion;
import de.instinct.shop.service.model.effects.HangarExpansion;
import de.instinct.shop.service.model.effects.NoEffect;
import de.instinct.shop.service.model.effects.ShipBlueprintUnlock;

public class ShopEffectModelRegistry {
	
	private static List<ShopItemEffect> effects;
	
	public static void init() {
		effects = new ArrayList<>();
		effects.add(new NoEffect());
		effects.add(new ShipBlueprintUnlock());
		effects.add(new HangarExpansion());
		effects.add(new ActiveShipSlotExpansion());
	}
	
	public static ShopItemEffect getEffect(ShopItemEffectData data) {
		for (ShopItemEffect effect : effects) {
			if (effect.acceptData(data)) return effect;
		}
		return null;
	}

}
