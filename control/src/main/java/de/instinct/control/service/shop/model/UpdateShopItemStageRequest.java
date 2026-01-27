package de.instinct.control.service.shop.model;

import lombok.Data;

@Data
public class UpdateShopItemStageRequest {
	
	private String itemId;
	private String id;
	private String newPrice;
	private String newDescription;
	private String newType;
	private String newData;

}
