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
public class ShopData {
	
	private List<Purchase> purchaseHistory;
	private List<ShopCategory> categories;

}
