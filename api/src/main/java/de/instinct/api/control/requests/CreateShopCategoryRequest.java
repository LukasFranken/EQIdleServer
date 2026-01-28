package de.instinct.api.control.requests;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class CreateShopCategoryRequest {

	private String name;
	
}
