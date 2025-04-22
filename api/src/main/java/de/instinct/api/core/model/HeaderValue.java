package de.instinct.api.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeaderValue {
	
	private String key;
	private String value;

}
