package base.discovery.impl;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import base.discovery.Discovery;
import base.discovery.dto.RegistrationResponseCode;
import base.discovery.dto.ServiceRegistrationDTO;

public class RESTDiscovery implements Discovery {
	
	private WebClient client;
	
	public RESTDiscovery(String discoveryServerAddress, int discoveryServerPort) {
		client = WebClient.builder()
				.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1 * 1024 * 1024))
				.baseUrl("http://" + discoveryServerAddress + ":" + discoveryServerPort)
			    .build();
	}

	@Override
	public RegistrationResponseCode register(ServiceRegistrationDTO serviceRegistrationDTO) {
		return client.post()
				.uri("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(serviceRegistrationDTO)
				.retrieve()
				.bodyToMono(RegistrationResponseCode.class)
				.block();
	}

}
