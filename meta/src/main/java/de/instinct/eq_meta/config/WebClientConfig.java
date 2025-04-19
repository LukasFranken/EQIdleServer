package de.instinct.eq_meta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class WebClientConfig {
	
	@Value("${discoveryserver.address:missing_discoveryserveraddress}")
	private String discoveryAddress;
	
	@Value("${discoveryserver.port:missing_discoveryserverport}")
	private String discoveryPort;

}
