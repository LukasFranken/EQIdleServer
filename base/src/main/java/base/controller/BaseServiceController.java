package base.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import de.instinct.api.core.API;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;

@RequestMapping
public class BaseServiceController extends BaseController {
	
	public BaseServiceController() {
		API.discovery().connect();
	}
	
	public BaseServiceController(String tag, int port, String version) {
		try {
			API.discovery().register(ServiceRegistrationDTO.builder()
					.serviceTag(tag)
					.serviceUrl("http://eqgame.dev:" + port + "/" + tag)
					.serviceVersion(version)
					.build());
		} catch (Exception e) {
			System.out.println("Failed to register service: " + tag + " to discovery server");
		}
	}

}
