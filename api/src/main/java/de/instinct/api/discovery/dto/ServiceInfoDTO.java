package de.instinct.api.discovery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInfoDTO {
	
	private String serviceTag;
    private String serviceUrl;
    private String serviceVersion;
    private long lastAlivePingAgoMS;

}
