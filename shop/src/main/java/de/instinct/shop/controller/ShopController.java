package de.instinct.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import base.controller.BaseServiceController;
import de.instinct.api.core.API;
import de.instinct.api.shop.dto.BuyResponse;
import de.instinct.api.shop.dto.ShopData;
import de.instinct.api.shop.dto.ShopInitializationResponseCode;
import de.instinct.shop.service.ShopService;
import de.instinct.shop.service.impl.ShopServiceImpl;

@RestController
@RequestMapping("/shop")
public class ShopController extends BaseServiceController {

	private final ShopService service;

	public ShopController(@Value("${server.port}") int serverPort, @Value("${application.version}") String version) {
		super("shop", serverPort, version);
		service = new ShopServiceImpl();
	}
	
	@Override
	protected void connectToAPIs() {
		API.meta().connect();
		API.shipyard().connect();
	}
	
	@GetMapping("/init/{token}")
	public ResponseEntity<ShopInitializationResponseCode> init(@PathVariable String token) {
		return ResponseEntity.ok(service.init(token));
	}
	
	@GetMapping("/data/{token}")
	public ResponseEntity<ShopData> data(@PathVariable String token) {
		return ResponseEntity.ok(service.getShopData(token));
	}
	
	@PostMapping("/buy/{token}/{itemId}")
	public ResponseEntity<BuyResponse> buy(@PathVariable String token, @PathVariable int itemId) {
		return ResponseEntity.ok(service.buy(token, itemId));
	}
	
}
