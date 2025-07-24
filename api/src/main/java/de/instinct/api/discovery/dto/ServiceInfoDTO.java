package de.instinct.api.discovery.dto;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ServiceInfoDTO {
	
	private String serviceTag;
	private String serviceProtocol;
	private String serviceAddress;
	private int servicePort;
	private String serviceEndpoint;
    private String serviceVersion;
    private long lastAlivePingAgoMS;

}
