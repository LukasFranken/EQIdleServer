package de.instinct.control.service.shop;

import org.springframework.ui.Model;

import de.instinct.control.service.shop.model.CreateShopCategoryRequest;
import de.instinct.control.service.shop.model.CreateShopCategoryResponse;
import de.instinct.control.service.shop.model.CreateShopItemRequest;
import de.instinct.control.service.shop.model.CreateShopItemResponse;

public interface ShopControlService {
	
	void setModel(Model model);
	
	void prepareShopTable(Model model, String id);

	CreateShopCategoryResponse createCategory(CreateShopCategoryRequest request);

	CreateShopItemResponse createItem(CreateShopItemRequest request);

}
