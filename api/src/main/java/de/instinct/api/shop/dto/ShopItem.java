package de.instinct.api.shop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopItem {
	
	private int id;
	private String name;
	private List<ShopItemStage> stages;

}
