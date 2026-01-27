package de.instinct.shop.service.model;

import de.instinct.api.shop.dto.item.ShopItemEffectData;

public interface ShopItemEffect {
	
	boolean acceptData(ShopItemEffectData data);
	
	String preconditionNotMetMessage(String token);
	
	void applyEffect(String token);

}
