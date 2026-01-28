package de.instinct.api.control.requests;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class UpdateShopItemStageRequest {
	
	private String itemId;
	private String id;
	private String newPrice;
	private String newDescription;
	private String newType;
	private String newData;

}
