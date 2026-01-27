package de.instinct.api.shop.dto.item;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShopItemStage {
	
	private int id;
	private String description;
	private long price;
	private ShopItemEffectData effectData;

}
