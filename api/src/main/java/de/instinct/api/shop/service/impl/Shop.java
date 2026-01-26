package de.instinct.api.shop.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.shop.dto.BuyResponse;
import de.instinct.api.shop.dto.PlayerShopData;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.ShopInitializationResponseCode;
import de.instinct.api.shop.dto.ShopSaveResponseCode;
import de.instinct.api.shop.service.ShopInterface;

public class Shop extends BaseService implements ShopInterface {

	public Shop() {
		super("shop");
	}
	
	@Override
	public void connect() {
		super.loadURL();
		super.connect();
	}

	@Override
	public ShopInitializationResponseCode init(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("init")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShopInitializationResponseCode.class);
	}
	
	@Override
	public ShopData shop() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("shop")
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShopData.class);
	}

	@Override
	public PlayerShopData data(String token) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("data")
				.pathVariable(token)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, PlayerShopData.class);
	}
	
	@Override
	public String load() {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("load")
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, String.class);
	}

	@Override
	public ShopSaveResponseCode save(ShopData data) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("save")
				.payload(data)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, ShopSaveResponseCode.class);
	}

	@Override
	public BuyResponse buy(String token, int itemId) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("buy")
				.pathVariable(token + "/" + itemId)
				.build());
		return response.contentEquals("") ? null : ObjectJSONMapper.mapJSON(response, BuyResponse.class);
	}
	
}
