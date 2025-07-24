package de.instinct.api.meta.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ResourceAmount {
	
	private Resource type;
	private long amount;

}
