package de.instinct.shop.service;

import de.instinct.api.shop.dto.BuyResponse;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.ShopInitializationResponseCode;

public interface ShopService {

	ShopInitializationResponseCode init(String token);

	ShopData getShopData(String token);

	BuyResponse buy(String token, int itemId);

}
