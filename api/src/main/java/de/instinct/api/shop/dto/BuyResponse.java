package de.instinct.api.shop.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class BuyResponse {
	
	private BuyResponseCode code;
	private String message;

}
