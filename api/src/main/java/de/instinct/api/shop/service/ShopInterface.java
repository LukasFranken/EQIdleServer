package de.instinct.api.shop.service;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.shop.dto.BuyResponse;
import de.instinct.api.shop.dto.PlayerShopData;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.ShopInitializationResponseCode;
import de.instinct.api.shop.dto.ShopSaveResponseCode;

public interface ShopInterface extends BaseServiceInterface {
	
	ShopInitializationResponseCode init(String token);

	ShopData shop();
	
	PlayerShopData data(String token);
	
	String load();
	
	ShopSaveResponseCode save(ShopData data);

	BuyResponse buy(String token, int itemId);

}
