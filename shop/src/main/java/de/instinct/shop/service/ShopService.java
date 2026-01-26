package de.instinct.shop.service;

import de.instinct.api.shop.dto.BuyResponse;
import de.instinct.api.shop.dto.PlayerShopData;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.ShopInitializationResponseCode;
import de.instinct.api.shop.dto.ShopSaveResponseCode;

public interface ShopService {

	ShopInitializationResponseCode init(String token);

	ShopData getBaseData();
	
	void loadBaseData();
	
	ShopSaveResponseCode saveBaseData(ShopData data);
	
	PlayerShopData getPlayerShopData(String token);

	BuyResponse buy(String token, int itemId);

}
