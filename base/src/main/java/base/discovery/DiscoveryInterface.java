package base.discovery;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import base.discovery.dto.RegistrationResponseCode;
import base.discovery.dto.ServiceRegistrationDTO;

public class DiscoveryInterface {
	
	private WebClient discoveryClient;
	
	public DiscoveryInterface(String discoveryServerAddress, int discoveryServerPort) {
		discoveryClient = WebClient.builder()
				.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1 * 1024 * 1024))
				.baseUrl("http://" + discoveryServerAddress + ":" + discoveryServerPort)
			    .build();
	}

	public RegistrationResponseCode register(ServiceRegistrationDTO serviceRegistrationDTO) {
		return discoveryClient.post()
				.uri("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(serviceRegistrationDTO)
				.retrieve()
				.bodyToMono(RegistrationResponseCode.class)
				.block();
	}

}
