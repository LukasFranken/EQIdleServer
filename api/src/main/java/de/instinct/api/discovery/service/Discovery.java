package de.instinct.api.discovery.service;

import org.springframework.http.MediaType;

import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.discovery.dto.RegistrationResponseCode;
import de.instinct.api.discovery.dto.ServiceInfoDTO;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;
import de.instinct.api.discovery.service.impl.DiscoveryInterface;

public class Discovery extends BaseService implements DiscoveryInterface {
	
	public Discovery() {
		super.url = "http://eqgame.dev:6000/discovery";
		connect();
	}

	@Override
	public RegistrationResponseCode register(ServiceRegistrationDTO serviceRegistrationDTO) {
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
		return webClient.get()
				.uri("/single/" + tag)
				.retrieve()
				.bodyToMono(ServiceInfoDTO.class)
				.block();
	}

}
