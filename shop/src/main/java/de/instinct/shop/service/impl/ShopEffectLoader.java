package de.instinct.shop.service.impl;

import java.util.HashMap;

import de.instinct.api.core.API;
import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.shop.service.model.ShopItemEffect;

public class ShopEffectLoader {
	
	private static HashMap<Integer, ShopItemEffect> shopItemEffects;
	
	public static HashMap<Integer, ShopItemEffect> getShopItemEffects() {
		if (shopItemEffects == null) loadShopItemEffects();
		return shopItemEffects;
	}
	
	private static void loadShopItemEffects() {
		shopItemEffects = new HashMap<>();
		shopItemEffects.put(0, new ShopItemEffect() {

			@Override
			public String preconditionMetMessage(String token, int stage) {
				return null;
			}

			@Override
			public void applyEffect(String token, int stage) {
				switch (stage) {
				case 0:
					API.shipyard().hangar(token, 2);
					break;
				case 1:
					API.shipyard().hangar(token, 2);
					break;
				case 2:
					API.shipyard().hangar(token, 2);
					break;
				case 3:
					API.shipyard().hangar(token, 3);
					break;
				case 4:
					API.shipyard().hangar(token, 4);
					break;
				}
			}

		});
		shopItemEffects.put(1, new ShopItemEffect() {

			@Override
			public String preconditionMetMessage(String token, int stage) {
				return null;
			}

			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().active(token, 1);
			}

		});
		shopItemEffects.put(2, new ShopItemEffect() {

			@Override
			public String preconditionMetMessage(String token, int stage) {
				PlayerShipyardData shipyard = API.shipyard().data(token);
				if (shipyard.getSlots() > shipyard.getShips().size())
					return null;
				return "Insufficient Hangar Space";
			}

			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().add(token, "turtle");
			}

		});
		shopItemEffects.put(3, new ShopItemEffect() {

			@Override
			public String preconditionMetMessage(String token, int stage) {
				PlayerShipyardData shipyard = API.shipyard().data(token);
				if (shipyard.getSlots() > shipyard.getShips().size())
					return null;
				return "Insufficient Hangar Space";
			}

			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().add(token, "eel");
			}

		});
		shopItemEffects.put(4, new ShopItemEffect() {

			@Override
			public String preconditionMetMessage(String token, int stage) {
				PlayerShipyardData shipyard = API.shipyard().data(token);
				if (shipyard.getSlots() > shipyard.getShips().size())
					return null;
				return "Insufficient Hangar Space";
			}

			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().add(token, "shark");
			}

		});
		shopItemEffects.put(5, new ShopItemEffect() {

			@Override
			public String preconditionMetMessage(String token, int stage) {
				PlayerShipyardData shipyard = API.shipyard().data(token);
				if (shipyard.getSlots() > shipyard.getShips().size())
					return null;
				return "Insufficient Hangar Space";
			}

			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().add(token, "shellshock");
			}

		});
		shopItemEffects.put(6, new ShopItemEffect() {

			@Override
			public String preconditionMetMessage(String token, int stage) {
				PlayerShipyardData shipyard = API.shipyard().data(token);
				if (shipyard.getSlots() > shipyard.getShips().size())
					return null;
				return "Insufficient Hangar Space";
			}

			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().add(token, "fallout");
			}

		});
	}

}
