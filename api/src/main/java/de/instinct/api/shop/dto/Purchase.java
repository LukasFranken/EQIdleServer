package de.instinct.api.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
	
	private String userToken;
	private int itemId;
	private int stage;
	private long timestamp;
	private long cost;

}
