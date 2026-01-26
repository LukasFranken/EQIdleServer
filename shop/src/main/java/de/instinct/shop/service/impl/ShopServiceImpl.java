package de.instinct.shop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.instinct.api.core.API;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.meta.dto.Resource;
import de.instinct.api.meta.dto.ResourceAmount;
import de.instinct.api.meta.dto.ResourceData;
import de.instinct.api.shop.dto.BuyResponse;
import de.instinct.api.shop.dto.BuyResponseCode;
import de.instinct.api.shop.dto.PlayerShopData;
import de.instinct.api.shop.dto.Purchase;
import de.instinct.api.shop.dto.ShopCategory;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.ShopInitializationResponseCode;
import de.instinct.api.shop.dto.ShopItem;
import de.instinct.api.shop.dto.ShopItemStage;
import de.instinct.api.shop.dto.ShopSaveResponseCode;
import de.instinct.base.file.FileManager;
import de.instinct.shop.service.ShopService;
import de.instinct.shop.service.model.ShopBaseData;

@Service
public class ShopServiceImpl implements ShopService {
	
	private ShopData shopData;
	private Map<String, PlayerShopData> playerShops;
	
	public ShopServiceImpl() {
		playerShops = new HashMap<>();
	}

	@Override
	public ShopInitializationResponseCode init(String token) {
		if (playerShops.get(token) != null) return ShopInitializationResponseCode.ALREADY_INITIALIZED;
		PlayerShopData initShopData = new PlayerShopData();
		initShopData.setPurchaseHistory(new ArrayList<>());
		playerShops.put(token, initShopData);
		return ShopInitializationResponseCode.SUCCESS;
	}
	
	@Override
	public PlayerShopData getPlayerShopData(String token) {
		PlayerShopData shop = playerShops.get(token);
		if (shop == null) {
			init(token);
			shop = playerShops.get(token);
		}
		return shop;
	}
	
	@Override
	public ShopData getBaseData() {
		if (shopData == null) {
			loadBaseData();
		}
		return shopData;
	}
	
	@Override
	public void loadBaseData() {
		shopData = new ShopData();
		ShopBaseData baseData = ObjectJSONMapper.mapJSON(FileManager.loadFile("base.data"), ShopBaseData.class);
		shopData.setCategories(baseData.getCategories());
	}
	
	@Override
	public ShopSaveResponseCode saveBaseData(ShopData data) {
		shopData = data;
		saveBaseData();
		return ShopSaveResponseCode.SUCCESS;
	}
	
	private void saveBaseData() {
		ShopBaseData baseData = new ShopBaseData();
		baseData.setCategories(shopData.getCategories());
		FileManager.saveFile("base.data", ObjectJSONMapper.mapObject(baseData));
	}

	@Override
	public BuyResponse buy(String token, int itemId) {
		PlayerShopData playerShop = playerShops.get(token);
		if (playerShop == null) {
			BuyResponse response = new BuyResponse();
			response.setCode(BuyResponseCode.INVALID_TOKEN);
			return response;
		}
		ShopItem item = null;
		for (ShopCategory category : shopData.getCategories()) {
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
		
		int stagesBought = playerShop.getPurchaseHistory().stream()
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
		
		String preconditionMessage = ShopEffectLoader.getShopItemEffects().get(item.getId()).preconditionMetMessage(token, stagesBought);
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
		ShopEffectLoader.getShopItemEffects().get(item.getId()).applyEffect(token, stagesBought);
		Purchase purchase = new Purchase();
		purchase.setUserToken(token);
		purchase.setItemId(itemId);
		purchase.setStage(stagesBought);
		purchase.setTimestamp(System.currentTimeMillis());
		purchase.setCost(firstUnboughtStage.getPrice());
		playerShop.getPurchaseHistory().add(purchase);
		
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
