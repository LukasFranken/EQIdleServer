package de.instinct.api.shop.service;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.shop.dto.BuyResponse;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.ShopInitializationResponseCode;

public interface ShopInterface extends BaseServiceInterface {
	
	ShopInitializationResponseCode init(String token);

	ShopData data(String token);

	BuyResponse buy(String token, int itemId);

}
