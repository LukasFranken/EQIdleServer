package de.instinct.control.service.shop.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import de.instinct.api.core.API;
import de.instinct.api.shop.dto.ShopCategory;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.ShopItem;
import de.instinct.control.component.table.Table;
import de.instinct.control.component.table.TableCell;
import de.instinct.control.component.table.TableHeader;
import de.instinct.control.component.table.TableRow;
import de.instinct.control.service.base.model.Link;
import de.instinct.control.service.shop.ShopControlService;
import de.instinct.control.service.shop.model.CreateShopCategoryRequest;
import de.instinct.control.service.shop.model.CreateShopCategoryResponse;
import de.instinct.control.service.shop.model.CreateShopItemRequest;
import de.instinct.control.service.shop.model.CreateShopItemResponse;

@Service
public class ShopControlServiceImpl implements ShopControlService {
	
	private ShopData shopData;
	
	@Override
	public void setModel(Model model) {
		shopData = API.shop().shop();
		List<Link> links = new ArrayList<>();
		for (ShopCategory shopCategory : shopData.getCategories()) {
			links.add(new Link(Integer.toString(shopCategory.getId()), "/shop/module/" + Integer.toString(shopCategory.getId()), shopCategory.getName()));
		}
	    model.addAttribute("ids", links);
	}
	
	@Override
	public void prepareShopTable(Model model, String id) {
		List<TableHeader> headers = new ArrayList<>();
		headers.add(TableHeader.builder()
				.label("ID")
				.className("id-column")
				.build());
		headers.add(TableHeader.builder()
				.label("Name")
				.build());
		headers.add(TableHeader.builder()
				.label("Stages")
				.build());
		headers.add(TableHeader.builder()
				.label("")
				.build());
		
		List<TableRow> rows = new ArrayList<>();
		for (ShopCategory shopCategory : shopData.getCategories()) {
			if (shopCategory.getId() == Integer.parseInt(id)) {
				for (ShopItem item : shopCategory.getItems()) {
					List<TableCell> cells = new ArrayList<>();
					cells.add(TableCell.builder().value(String.valueOf(item.getId())).className("id-column").build());
					cells.add(TableCell.builder().value(item.getName()).build());
					cells.add(TableCell.builder().value(String.valueOf(item.getStages().size())).build());
					cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("shop-item-actions").attributes("param-modal=shop-itemmodal, param=" + item.getId() + ", init-method=initializeShopItemModal").build());
					rows.add(TableRow.builder()
							.cells(cells)
							.build());
				}
			}
		}
		
    	model.addAttribute("items", Table.builder()
    			.headers(headers)
    			.rows(rows)
				.build());
    	model.addAttribute("categoryId", id);
	}

	@Override
	public CreateShopCategoryResponse createCategory(CreateShopCategoryRequest request) {
		for (ShopCategory category : shopData.getCategories()) {
			if (category.getName().equalsIgnoreCase(request.getName())) {
				return CreateShopCategoryResponse.NAME_TAKEN;
			}
		}
		ShopCategory newCategory = new ShopCategory();
		newCategory.setId(shopData.getCategories().get(shopData.getCategories().size() - 1).getId() + 1);
		newCategory.setName(request.getName());
		newCategory.setItems(new ArrayList<>());
		shopData.getCategories().add(newCategory);
		API.shop().save(shopData);
		return CreateShopCategoryResponse.SUCCESS;
	}

	@Override
	public CreateShopItemResponse createItem(CreateShopItemRequest request) {
		ShopCategory targetCategory = null;
		for (ShopCategory category : shopData.getCategories()) {
			if (category.getId() == request.getCategoryId()) {
				targetCategory = category;
			}
		}
		if (targetCategory == null) return CreateShopItemResponse.CATEGORY_ID_NOT_FOUND;
		
		for (ShopItem item : targetCategory.getItems()) {
			if (item.getName().equalsIgnoreCase(request.getName())) {
				return CreateShopItemResponse.ITEM_NAME_EXISTS;
			}
		}
		
		ShopItem newItem = new ShopItem();
		newItem.setId(shopData.getNextItemId());
		newItem.setName(request.getName());
		newItem.setStages(new ArrayList<>());
		targetCategory.getItems().add(newItem);
		shopData.setNextItemId(shopData.getNextItemId() + 1);
		API.shop().save(shopData);
		return CreateShopItemResponse.SUCCESS;
	}

}
