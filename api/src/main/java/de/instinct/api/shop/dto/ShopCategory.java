package de.instinct.api.shop.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShopCategory {
	
	private String name;
	private List<ShopItem> items;

}
