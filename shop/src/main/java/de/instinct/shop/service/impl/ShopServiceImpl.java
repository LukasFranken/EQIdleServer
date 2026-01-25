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
import de.instinct.api.shipyard.dto.PlayerShipyardData;
import de.instinct.api.shop.dto.BuyResponse;
import de.instinct.api.shop.dto.BuyResponseCode;
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
			public String preconditionMetMessage(String token, int stage) {
				return null;
			}
			
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
					API.shipyard().hangar(token, 2);
					break;
				case 3:
					API.shipyard().hangar(token, 3);
					break;
				case 4:
					API.shipyard().hangar(token, 4);
					break;
				}
			}
			
		});
		shopItemEffects.put(1, new ShopItemEffect() {
			
			@Override
			public String preconditionMetMessage(String token, int stage) {
				return null;
			}
			
			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().active(token, 1);
			}
			
		});
		shopItemEffects.put(2, new ShopItemEffect() {
			
			@Override
			public String preconditionMetMessage(String token, int stage) {
				PlayerShipyardData shipyard = API.shipyard().data(token);
				if (shipyard.getSlots() > shipyard.getShips().size()) return null;
				return "Insufficient Hangar Space";
			}
			
			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().add(token, "turtle");
			}
			
		});
		shopItemEffects.put(3, new ShopItemEffect() {
			
			@Override
			public String preconditionMetMessage(String token, int stage) {
				PlayerShipyardData shipyard = API.shipyard().data(token);
				if (shipyard.getSlots() > shipyard.getShips().size()) return null;
				return "Insufficient Hangar Space";
			}
			
			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().add(token, "eel");
			}
			
		});
		shopItemEffects.put(4, new ShopItemEffect() {
			
			@Override
			public String preconditionMetMessage(String token, int stage) {
				PlayerShipyardData shipyard = API.shipyard().data(token);
				if (shipyard.getSlots() > shipyard.getShips().size()) return null;
				return "Insufficient Hangar Space";
			}
			
			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().add(token, "shark");
			}
			
		});
		shopItemEffects.put(5, new ShopItemEffect() {
			
			@Override
			public String preconditionMetMessage(String token, int stage) {
				PlayerShipyardData shipyard = API.shipyard().data(token);
				if (shipyard.getSlots() > shipyard.getShips().size()) return null;
				return "Insufficient Hangar Space";
			}
			
			@Override
			public void applyEffect(String token, int stage) {
				API.shipyard().add(token, "shellshock");
			}
			
		});
	}

	@Override
	public ShopInitializationResponseCode init(String token) {
		if (shops.get(token) != null) return ShopInitializationResponseCode.ALREADY_INITIALIZED;
		ShopData initShopData = new ShopData();
		initShopData.setPurchaseHistory(new ArrayList<>());
		initShopData.setCategories(loadInitialShopCategories());
		shops.put(token, initShopData);
		return ShopInitializationResponseCode.SUCCESS;
	}
	
	private List<ShopCategory> loadInitialShopCategories() {
		List<ShopCategory> categories = new ArrayList<>();
		
		List<ShopItem> shipyardItems = new ArrayList<>();
		List<ShopItemStage> hangarStages = new ArrayList<>();
		ShopItemStage hangarStage1 = new ShopItemStage();
		hangarStage1.setDescription("1 -> 3");
		hangarStage1.setPrice(200);
		hangarStages.add(hangarStage1);
		ShopItemStage hangarStage2 = new ShopItemStage();
		hangarStage2.setDescription("3 -> 5");
		hangarStage2.setPrice(1000);
		hangarStages.add(hangarStage2);
		ShopItemStage hangarStage3 = new ShopItemStage();
		hangarStage3.setDescription("5 -> 7");
		hangarStage3.setPrice(5000);
		hangarStages.add(hangarStage3);
		ShopItemStage hangarStage4 = new ShopItemStage();
		hangarStage4.setDescription("7 -> 10");
		hangarStage4.setPrice(25000);
		hangarStages.add(hangarStage4);
		ShopItemStage hangarStage5 = new ShopItemStage();
		hangarStage5.setDescription("10 -> 14");
		hangarStage5.setPrice(100000);
		hangarStages.add(hangarStage5);
		ShopItem hangarItem = new ShopItem();
		hangarItem.setId(0);
		hangarItem.setName("Hangar Space");
		hangarItem.setStages(hangarStages);
		shipyardItems.add(hangarItem);
		
		List<ShopItemStage> activeShipsStages = new ArrayList<>();
		ShopItemStage activeShipsStage1 = new ShopItemStage();
		activeShipsStage1.setDescription("1 -> 2");
		activeShipsStage1.setPrice(1000);
		activeShipsStages.add(activeShipsStage1);
		ShopItemStage activeShipsStage2 = new ShopItemStage();
		activeShipsStage2.setDescription("2 -> 3");
		activeShipsStage2.setPrice(5000);
		activeShipsStages.add(activeShipsStage2);
		ShopItemStage activeShipsStage3 = new ShopItemStage();
		activeShipsStage3.setDescription("3 -> 4");
		activeShipsStage3.setPrice(25000);
		activeShipsStages.add(activeShipsStage3);
		ShopItem activeShipsItem = new ShopItem();
		activeShipsItem.setId(1);
		activeShipsItem.setName("Active Ships");
		activeShipsItem.setStages(activeShipsStages);
		shipyardItems.add(activeShipsItem);
		
		ShopCategory shipyardCategory = new ShopCategory();
		shipyardCategory.setName("Shipyard");
		shipyardCategory.setItems(shipyardItems);
		categories.add(shipyardCategory);
		
		List<ShopItem> shipBlueprintItems = new ArrayList<>();
		List<ShopItemStage> turtleStages = new ArrayList<>();
		ShopItemStage turtleStage = new ShopItemStage();
		turtleStage.setDescription("");
		turtleStage.setPrice(1000);
		turtleStages.add(turtleStage);
		ShopItem turtleItem = new ShopItem();
		turtleItem.setId(2);
		turtleItem.setName("Turtle");
		turtleItem.setStages(turtleStages);
		shipBlueprintItems.add(turtleItem);
		
		List<ShopItemStage> sharkStages = new ArrayList<>();
		ShopItemStage sharkStage = new ShopItemStage();
		sharkStage.setDescription("");
		sharkStage.setPrice(1000);
		sharkStages.add(sharkStage);
		ShopItem sharkItem = new ShopItem();
		sharkItem.setId(3);
		sharkItem.setName("Shark");
		sharkItem.setStages(sharkStages);
		shipBlueprintItems.add(sharkItem);
		
		List<ShopItemStage> eelStages = new ArrayList<>();
		ShopItemStage eelStage = new ShopItemStage();
		eelStage.setDescription("");
		eelStage.setPrice(1000);
		eelStages.add(eelStage);
		ShopItem eelItem = new ShopItem();
		eelItem.setId(4);
		eelItem.setName("Eel");
		eelItem.setStages(eelStages);
		shipBlueprintItems.add(eelItem);
		
		ShopCategory shipBlueprintsCategory = new ShopCategory();
		shipBlueprintsCategory.setName("Ship Blueprints");
		shipBlueprintsCategory.setItems(shipBlueprintItems);
		categories.add(shipBlueprintsCategory);
		
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
		if (shop == null) {
			BuyResponse response = new BuyResponse();
			response.setCode(BuyResponseCode.INVALID_TOKEN);
			return response;
		}
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
		if (item == null) {
			BuyResponse response = new BuyResponse();
			response.setCode(BuyResponseCode.ITEM_NOT_FOUND);
			return response;
		}
		
		int stagesBought = shop.getPurchaseHistory().stream()
				.filter(p -> p.getItemId() == itemId)
				.toList()
				.size();
		if (stagesBought >= item.getStages().size()) {
			BuyResponse response = new BuyResponse();
			response.setCode(BuyResponseCode.ALREADY_BOUGHT);
			return response;
		}
		
		ShopItemStage firstUnboughtStage = item.getStages().get(stagesBought);
		ResourceData playerResources = API.meta().resources(token);
		if (getPlayerResource(playerResources, Resource.CREDITS) < firstUnboughtStage.getPrice()) {
			BuyResponse response = new BuyResponse();
			response.setCode(BuyResponseCode.NOT_ENOUGH_CURRENCY);
			response.setMessage("Insufficient credits");
			return response;
		}
		
		String preconditionMessage = shopItemEffects.get(item.getId()).preconditionMetMessage(token, stagesBought);
		if (preconditionMessage != null) {
			BuyResponse response = new BuyResponse();
			response.setCode(BuyResponseCode.PRECONDITION_NOT_MET);
			response.setMessage(preconditionMessage);
			return response;
		}
		
		ResourceData resourceData = new ResourceData();
		resourceData.setResources(new ArrayList<>());
		ResourceAmount credits = new ResourceAmount();
		credits.setType(Resource.CREDITS);
		credits.setAmount(-firstUnboughtStage.getPrice());
		resourceData.getResources().add(credits);
		API.meta().addResources(token, resourceData);
		shopItemEffects.get(item.getId()).applyEffect(token, stagesBought);
		Purchase purchase = new Purchase();
		purchase.setUserToken(token);
		purchase.setItemId(itemId);
		purchase.setStage(stagesBought);
		purchase.setTimestamp(System.currentTimeMillis());
		purchase.setCost(firstUnboughtStage.getPrice());
		shop.getPurchaseHistory().add(purchase);
		
		BuyResponse response = new BuyResponse();
		response.setCode(BuyResponseCode.SUCCESS);
		return response;
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
