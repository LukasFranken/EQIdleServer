package de.instinct.api.shop.dto.item;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShopItemEffectData {

	private ShopItemEffectType type;
	private String data;
	
}
