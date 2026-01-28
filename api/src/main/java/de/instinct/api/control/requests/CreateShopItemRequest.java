package de.instinct.api.control.requests;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class CreateShopItemRequest {
	
	private String name;
	private int categoryId;

}
