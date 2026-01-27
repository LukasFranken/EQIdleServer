package de.instinct.control.service.shop;

import java.util.List;

import org.springframework.ui.Model;

import de.instinct.control.service.shop.model.CreateShopCategoryRequest;
import de.instinct.control.service.shop.model.CreateShopCategoryResponse;
import de.instinct.control.service.shop.model.CreateShopItemRequest;
import de.instinct.control.service.shop.model.CreateShopItemResponse;
import de.instinct.control.service.shop.model.CreateShopItemStageRequest;
import de.instinct.control.service.shop.model.CreateShopItemStageResponse;
import de.instinct.control.service.shop.model.DeleteShopItemStageRequest;
import de.instinct.control.service.shop.model.DeleteShopItemStageResponse;
import de.instinct.control.service.shop.model.UpdateShopItemStageRequest;
import de.instinct.control.service.shop.model.UpdateShopItemStageResponse;

public interface ShopControlService {
	
	void setModel(Model model);
	
	void prepareShopTable(Model model, String id);

	CreateShopCategoryResponse createCategory(CreateShopCategoryRequest request);

	CreateShopItemResponse createItem(CreateShopItemRequest request);
	
	CreateShopItemStageResponse createItemStage(CreateShopItemStageRequest request);
	
	UpdateShopItemStageResponse updateItemStage(UpdateShopItemStageRequest request);

	DeleteShopItemStageResponse deleteItemStage(DeleteShopItemStageRequest request);

	void prepareItemModal(Model model, String id);

	List<String> getEffectTypes();

}
