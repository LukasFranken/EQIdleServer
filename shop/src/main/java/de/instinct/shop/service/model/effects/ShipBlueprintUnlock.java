package de.instinct.shop.service.model.effects;

import de.instinct.api.core.API;
import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shop.dto.item.ShopItemEffectData;
import de.instinct.api.shop.dto.item.ShopItemEffectType;
import de.instinct.shop.service.model.ShopItemEffect;
import lombok.Data;

@Data
public class ShipBlueprintUnlock implements ShopItemEffect {
	
	private ShopItemEffectData data;
	
	@Override
	public boolean acceptData(ShopItemEffectData data) {
		this.data = data;
		return data.getType() == ShopItemEffectType.SHIP_BLUEPRINT_UNLOCK;
	}

	@Override
	public String preconditionNotMetMessage(String token) {
		PlayerShipyardData shipyard = API.shipyard().data(token);
		if (shipyard.getSlots() > shipyard.getShips().size()) return null;
		return "Insufficient Hangar Space";
	}

	@Override
	public void applyEffect(String token) {
		API.shipyard().add(token, data.getData());
	}

}
