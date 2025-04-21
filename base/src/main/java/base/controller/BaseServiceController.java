package base.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;

@RequestMapping
public class BaseServiceController extends BaseController {
	
	public BaseServiceController(String tag, int port, String version) {
		API.initialize(APIConfiguration.SERVER);
		API.discovery().connect();
		try {
			API.discovery().register(ServiceRegistrationDTO.builder()
					.serviceTag(tag)
					.serviceProtocol("http")
					.serviceAddress("eqgame.dev")
					.servicePort(port)
					.serviceEndpoint(tag)
					.serviceVersion(version)
					.build());
		} catch (Exception e) {
			System.out.println("Failed to register service: " + tag + " to discovery server");
		}
	}

}
