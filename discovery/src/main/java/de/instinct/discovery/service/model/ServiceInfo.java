package de.instinct.discovery.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceInfo {
	
	private String name;
	private String url;
	private String version;
	private long registrationTimestamp;
	private long lastAlivePing;

}
