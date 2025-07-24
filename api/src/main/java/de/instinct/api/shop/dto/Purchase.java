package de.instinct.api.shop.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class Purchase {
	
	private String userToken;
	private int itemId;
	private int stage;
	private long timestamp;
	private long cost;

}
