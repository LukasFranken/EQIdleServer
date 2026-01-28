package de.instinct.control.service.shop;

import java.util.List;

import org.springframework.ui.Model;

import de.instinct.api.control.requests.CreateShopCategoryRequest;
import de.instinct.api.control.requests.CreateShopCategoryResponse;
import de.instinct.api.control.requests.CreateShopItemRequest;
import de.instinct.api.control.requests.CreateShopItemResponse;
import de.instinct.api.control.requests.CreateShopItemStageRequest;
import de.instinct.api.control.requests.CreateShopItemStageResponse;
import de.instinct.api.control.requests.DeleteShopCategoryResponse;
import de.instinct.api.control.requests.DeleteShopItemResponse;
import de.instinct.api.control.requests.DeleteShopItemStageRequest;
import de.instinct.api.control.requests.DeleteShopItemStageResponse;
import de.instinct.api.control.requests.UpdateShopItemStageRequest;
import de.instinct.api.control.requests.UpdateShopItemStageResponse;

public interface ShopControlService {
	
	void setModel(Model model);
	
	void prepareShopTable(Model model, String id);

	CreateShopCategoryResponse createCategory(CreateShopCategoryRequest request);
	
	DeleteShopCategoryResponse deleteCategory(String id);

	CreateShopItemResponse createItem(CreateShopItemRequest request);
	
	DeleteShopItemResponse deleteItem(String id);
	
	CreateShopItemStageResponse createItemStage(CreateShopItemStageRequest request);
	
	UpdateShopItemStageResponse updateItemStage(UpdateShopItemStageRequest request);

	DeleteShopItemStageResponse deleteItemStage(DeleteShopItemStageRequest request);

	void prepareItemModal(Model model, String id);

	List<String> getEffectTypes();

}
