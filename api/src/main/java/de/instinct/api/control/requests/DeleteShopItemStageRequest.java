package de.instinct.api.control.requests;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class DeleteShopItemStageRequest {
	
	private String itemId;
	private String id;

}
