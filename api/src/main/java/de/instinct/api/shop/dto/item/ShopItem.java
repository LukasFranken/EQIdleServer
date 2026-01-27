package de.instinct.api.shop.dto.item;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShopItem {
	
	private int id;
	private String name;
	private List<ShopItemStage> stages;

}
