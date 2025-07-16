package de.instinct.shop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.instinct.api.core.API;
import de.instinct.api.meta.dto.Resource;
import de.instinct.api.meta.dto.ResourceAmount;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.shop.dto.BuyResponse;
import de.instinct.api.shop.dto.Purchase;
import de.instinct.api.shop.dto.ShopCategory;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.ShopInitializationResponseCode;
import de.instinct.api.shop.dto.ShopItem;
import de.instinct.api.shop.dto.ShopItemStage;
import de.instinct.shop.service.ShopService;
import de.instinct.shop.service.model.ShopItemEffect;

@Service
public class ShopServiceImpl implements ShopService {
	
	private Map<String, ShopData> shops;
	private Map<Integer, ShopItemEffect> shopItemEffects;
	
	public ShopServiceImpl() {
		shops = new HashMap<>();
		loadShopItemEffects();
	}

	private void loadShopItemEffects() {
		shopItemEffects = new HashMap<>();
		shopItemEffects.put(0, new ShopItemEffect() {
			
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
					API.shipyard().hangar(token, 3);
					break;
				}
			}
			
		});
		shopItemEffects.put(1, new ShopItemEffect() {
			
			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().active(token, 1);
			}
			
		});
	}

	@Override
	public ShopInitializationResponseCode init(String token) {
		if (shops.get(token) != null) return ShopInitializationResponseCode.ALREADY_INITIALIZED;
		shops.put(token, ShopData.builder()
				.purchaseHistory(new ArrayList<>())
				.categories(loadInitialShopData())
				.build());
		return ShopInitializationResponseCode.SUCCESS;
	}
	
	private List<ShopCategory> loadInitialShopData() {
		List<ShopItem> shipyardItems = new ArrayList<>();
		List<ShopItemStage> hangarStages = new ArrayList<>();
		hangarStages.add(ShopItemStage.builder()
				.description("1 -> 3")
				.price(1000)
				.build());
		hangarStages.add(ShopItemStage.builder()
				.description("3 -> 5")
				.price(2000)
				.build());
		hangarStages.add(ShopItemStage.builder()
				.description("5 -> 8")
				.price(5000)
				.build());
		shipyardItems.add(ShopItem.builder()
				.id(0)
				.name("Hangar Space")
				.stages(hangarStages)
				.build());
		
		List<ShopItemStage> activeShipsStages = new ArrayList<>();
		activeShipsStages.add(ShopItemStage.builder()
				.description("1 -> 2")
				.price(1000)
				.build());
		activeShipsStages.add(ShopItemStage.builder()
				.description("2 -> 3")
				.price(5000)
				.build());
		activeShipsStages.add(ShopItemStage.builder()
				.description("3 -> 4")
				.price(25000)
				.build());
		shipyardItems.add(ShopItem.builder()
				.id(1)
				.name("Active Ships")
				.stages(activeShipsStages)
				.build());
		
		ShopCategory shipyardCategory = ShopCategory.builder()
				.name("Shipyard")
				.items(shipyardItems)
				.build();
		
		List<ShopCategory> categories = new ArrayList<>();
		categories.add(shipyardCategory);
		return categories;
	}

	@Override
	public ShopData getShopData(String token) {
		ShopData shop = shops.get(token);
		if (shop == null) {
			init(token);
			shop = shops.get(token);
		}
		return shop;
	}

	@Override
	public BuyResponse buy(String token, int itemId) {
		ShopData shop = shops.get(token);
		if (shop == null) return BuyResponse.INVALID_TOKEN;
		ShopItem item = null;
		for (ShopCategory category : shop.getCategories()) {
			for (ShopItem shopItem : category.getItems()) {
				if (shopItem.getId() == itemId) {
					item = shopItem;
					break;
				}
			}
			if (item != null) break;
		}
		if (item == null) return BuyResponse.ITEM_NOT_FOUND;
		
		int stagesBought = shop.getPurchaseHistory().stream()
				.filter(p -> p.getItemId() == itemId)
				.toList()
				.size();
		if (stagesBought >= item.getStages().size()) return BuyResponse.ALREADY_BOUGHT;
		ShopItemStage firstUnboughtStage = item.getStages().get(stagesBought);
		ResourceData playerResources = API.meta().resources(token);
		if (getPlayerResource(playerResources, Resource.CREDITS) < firstUnboughtStage.getPrice()) return BuyResponse.NOT_ENOUGH_CURRENCY;
		
		ResourceData resourceData = ResourceData.builder()
				.resources(new ArrayList<>())
				.build();
		resourceData.getResources().add(ResourceAmount.builder()
				.type(Resource.CREDITS)
				.amount(-firstUnboughtStage.getPrice())
				.build());
		API.meta().addResources(token, resourceData);
		shopItemEffects.get(item.getId()).applyEffect(token, stagesBought);
		shop.getPurchaseHistory().add(Purchase.builder()
				.userToken(token)
				.itemId(itemId)
				.stage(stagesBought)
				.timestamp(System.currentTimeMillis())
				.cost(firstUnboughtStage.getPrice())
				.build());
		return BuyResponse.SUCCESS;
	}

	private long getPlayerResource(ResourceData playerResources, Resource type) {
		for (ResourceAmount resource : playerResources.getResources()) {
			if (resource.getType() == type) {
				return resource.getAmount();
			}
		}
		return 0;
	}

}
