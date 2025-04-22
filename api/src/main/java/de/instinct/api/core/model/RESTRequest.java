package de.instinct.api.core.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RESTRequest {
	
	private SupportedRequestType type;
	private String endpoint;
	private String pathVariable;
	private List<HeaderValue> requestHeader;
	private Object payload;

}