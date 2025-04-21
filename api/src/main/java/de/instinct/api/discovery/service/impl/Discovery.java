package de.instinct.api.discovery.service.impl;

import org.springframework.http.MediaType;

import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.URLBuilder;
import de.instinct.api.discovery.dto.RegistrationResponseCode;
import de.instinct.api.discovery.dto.ServiceInfoDTO;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;
import de.instinct.api.discovery.service.DiscoveryInterface;

public class Discovery extends BaseService implements DiscoveryInterface {
	
	public Discovery() {
		super("discovery");
		super.baseUrl = URLBuilder.build(ServiceInfoDTO.builder()
				.servicePort(6000)
				.serviceEndpoint("discovery")
				.build());
	}

	@Override
	public RegistrationResponseCode register(ServiceRegistrationDTO serviceRegistrationDTO) {
		if (!isConnected()) return null;
		return webClient.post()
				.uri("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(serviceRegistrationDTO)
				.retrieve()
				.bodyToMono(RegistrationResponseCode.class)
				.block();
	}
	
	@Override
	public ServiceInfoDTO discover(String tag) {
		if (!isConnected()) return null;
		return webClient.get()
				.uri("/single/" + tag)
				.retrieve()
				.bodyToMono(ServiceInfoDTO.class)
				.block();
	}

}
