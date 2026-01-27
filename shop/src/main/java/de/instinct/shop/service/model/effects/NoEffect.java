package de.instinct.shop.service.model.effects;

import de.instinct.api.shop.dto.item.ShopItemEffectData;
import de.instinct.api.shop.dto.item.ShopItemEffectType;
import de.instinct.shop.service.model.ShopItemEffect;
import lombok.Data;

@Data
public class NoEffect implements ShopItemEffect {
	
	@Override
	public boolean acceptData(ShopItemEffectData data) {
		return data.getType() == ShopItemEffectType.NONE;
	}

	@Override
	public String preconditionNotMetMessage(String token) {
		return null;
	}

	@Override
	public void applyEffect(String token) {}

}
