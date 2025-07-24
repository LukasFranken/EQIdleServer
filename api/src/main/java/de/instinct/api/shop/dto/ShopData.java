package de.instinct.api.shop.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ShopData {
	
	private List<Purchase> purchaseHistory;
	private List<ShopCategory> categories;

}
