package de.instinct.shop.service.model;

import java.util.List;

import de.instinct.api.shop.dto.ShopCategory;
import lombok.Data;

@Data
public class ShopBaseData {
	
	private List<ShopCategory> categories;

}
