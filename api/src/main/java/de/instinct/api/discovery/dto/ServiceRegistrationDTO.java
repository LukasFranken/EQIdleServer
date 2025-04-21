package de.instinct.api.discovery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegistrationDTO {
	
	private String serviceTag;
    private String serviceProtocol;
    private String serviceAddress;
    private int servicePort;
    private String serviceEndpoint;
    private String serviceVersion;

}
