package de.instinct.shop.service.model.effects;

import de.instinct.api.core.API;
import de.instinct.api.shop.dto.item.ShopItemEffectData;
import de.instinct.api.shop.dto.item.ShopItemEffectType;
import de.instinct.shop.service.model.ShopItemEffect;
import lombok.Data;

@Data
public class ActiveShipSlotExpansion implements ShopItemEffect {
	
	private ShopItemEffectData data;
	
	@Override
	public boolean acceptData(ShopItemEffectData data) {
		this.data = data;
		return data.getType() == ShopItemEffectType.SHIP_ACTIVE_SLOT_EXPANSION;
	}

	@Override
	public String preconditionNotMetMessage(String token) {
		return data.getData().matches("^[0-9]*$") ? null : "Invalid effect data for ActiveShipSlotExpansion effect. Data (" + data.getData() + ") is not an integer.";
	}

	@Override
	public void applyEffect(String token) {
		API.shipyard().active(token, Integer.parseInt(data.getData()));
	}

}
