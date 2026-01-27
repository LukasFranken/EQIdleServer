package de.instinct.control.service.shop.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import de.instinct.api.core.API;
import de.instinct.api.shop.dto.ShopCategory;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.item.ShopItem;
import de.instinct.api.shop.dto.item.ShopItemEffectData;
import de.instinct.api.shop.dto.item.ShopItemEffectType;
import de.instinct.api.shop.dto.item.ShopItemStage;
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
import de.instinct.control.service.shop.model.CreateShopItemStageRequest;
import de.instinct.control.service.shop.model.CreateShopItemStageResponse;
import de.instinct.control.service.shop.model.DeleteShopItemStageRequest;
import de.instinct.control.service.shop.model.DeleteShopItemStageResponse;
import de.instinct.control.service.shop.model.UpdateShopItemStageRequest;
import de.instinct.control.service.shop.model.UpdateShopItemStageResponse;

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
					cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("shop-item-actions").attributes("param-modal=shop-shopitemmodal, param=" + item.getId() + ", init-method=initializeShopItemModal").build());
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
	
	@Override
	public CreateShopItemStageResponse createItemStage(CreateShopItemStageRequest request) {
		ShopItem targetItem = getItem(Integer.parseInt(request.getId()));
		if (targetItem == null) return CreateShopItemStageResponse.ITEM_NOT_FOUND;
		
		ShopItemStage newStage = new ShopItemStage();
		newStage.setId(targetItem.getStages().size() > 0 ? targetItem.getStages().get(targetItem.getStages().size() - 1).getId() + 1 : 0);
		newStage.setPrice(0);
		newStage.setDescription("");
		newStage.setEffectData(new ShopItemEffectData());
		newStage.getEffectData().setType(ShopItemEffectType.NONE);
		newStage.getEffectData().setData("");
		
		targetItem.getStages().add(newStage);
		API.shop().save(shopData);
		return CreateShopItemStageResponse.SUCCESS;
	}
	
	@Override
	public UpdateShopItemStageResponse updateItemStage(UpdateShopItemStageRequest request) {
		ShopItem targetItem = getItem(Integer.parseInt(request.getItemId()));
		if (targetItem == null) return UpdateShopItemStageResponse.ITEM_NOT_FOUND;
		
		for (ShopItemStage stage : targetItem.getStages()) {
			if (stage.getId() == Integer.parseInt(request.getId())) {
				stage.setPrice(Long.parseLong(request.getNewPrice()));
				stage.setDescription(request.getNewDescription());
				stage.setEffectData(new ShopItemEffectData());
				stage.getEffectData().setType(ShopItemEffectType.valueOf(request.getNewType()));
				stage.getEffectData().setData(request.getNewData());
				API.shop().save(shopData);
				return UpdateShopItemStageResponse.SUCCESS;
			}
		}
		
		return UpdateShopItemStageResponse.STAGE_NOT_FOUND;
	}

	@Override
	public DeleteShopItemStageResponse deleteItemStage(DeleteShopItemStageRequest request) {
		ShopItem targetItem = getItem(Integer.parseInt(request.getItemId()));
		if (targetItem == null) return DeleteShopItemStageResponse.ITEM_NOT_FOUND;
		
		for (ShopItemStage stage : targetItem.getStages()) {
			if (stage.getId() == Integer.parseInt(request.getId())) {
				targetItem.getStages().remove(stage);
				API.shop().save(shopData);
				return DeleteShopItemStageResponse.SUCCESS;
			}
		}
		
		return DeleteShopItemStageResponse.STAGE_NOT_FOUND;
	}

	@Override
	public void prepareItemModal(Model model, String id) {
		model.addAttribute("id", id);
		ShopItem item = getItem(Integer.parseInt(id));
		if (item != null) {
			model.addAttribute("name", item.getName());
			prepareItemTable(model, item);
		}
	}
	
	private void prepareItemTable(Model model, ShopItem item) {
		List<TableHeader> headers = new ArrayList<>();
		headers.add(TableHeader.builder()
				.label("ID")
				.className("id-column")
				.build());
		headers.add(TableHeader.builder()
				.label("Price")
				.build());
		headers.add(TableHeader.builder()
				.label("Description")
				.build());
		headers.add(TableHeader.builder()
				.label("Effect Type")
				.build());
		headers.add(TableHeader.builder()
				.label("Effect Data")
				.build());
		headers.add(TableHeader.builder()
				.label("")
				.build());
		
		List<TableRow> rows = new ArrayList<>();
		for (ShopItemStage stage : item.getStages()) {
			List<TableCell> cells = new ArrayList<>();
			cells.add(TableCell.builder().value(String.valueOf(stage.getId())).className("id-column").build());
			cells.add(TableCell.builder().value(String.valueOf(stage.getPrice())).build());
			cells.add(TableCell.builder().value(stage.getDescription()).build());
			cells.add(TableCell.builder().value(stage.getEffectData().getType() != null ? stage.getEffectData().getType().toString() : "").build());
			cells.add(TableCell.builder().value(stage.getEffectData().getData()).build());
			cells.add(TableCell.builder().value("<button class=\"edit-btn\">Edit</button>").className("shop-item-stage-actions").build());
			rows.add(TableRow.builder()
					.cells(cells)
					.className("shop-item-stage-row")
					.build());
		}
		
    	model.addAttribute("stages", Table.builder()
    			.headers(headers)
    			.rows(rows)
				.build());
	}

	private ShopItem getItem(int id) {
		for (ShopCategory shopCategory : shopData.getCategories()) {
			for (ShopItem item : shopCategory.getItems()) {
				if (item.getId() == id) {
					return item;
				}		
			}
		}	
		return null;
	}

	@Override
	public List<String> getEffectTypes() {
		List<String> effectTypes = new ArrayList<>();
		for (ShopItemEffectType type : ShopItemEffectType.values()) {
			effectTypes.add(type.toString());
		}
		return effectTypes;
	}

}
