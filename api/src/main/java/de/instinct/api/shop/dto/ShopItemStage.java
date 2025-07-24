package de.instinct.api.shop.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShopItemStage {
	
	private String description;
	private long price;

}
