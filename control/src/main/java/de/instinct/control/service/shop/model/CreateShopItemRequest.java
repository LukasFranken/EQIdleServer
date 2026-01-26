package de.instinct.control.service.shop.model;

import lombok.Data;

@Data
public class CreateShopItemRequest {
	
	private String name;
	private int categoryId;

}
