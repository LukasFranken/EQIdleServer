package base.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import base.discovery.DiscoveryInterface;
import base.discovery.dto.ServiceRegistrationDTO;

@RequestMapping
public class BaseServiceController extends BaseController {
	
	private DiscoveryInterface discovery = new DiscoveryInterface("localhost", 6000);
	
	public BaseServiceController(String tag, int port, String version) {
		try {
			discovery.register(ServiceRegistrationDTO.builder()
					.serviceTag(tag)
					.serviceUrl("http://eqgame.dev:" + port + "/" + tag)
					.serviceVersion(version)
					.build());
		} catch (Exception e) {
			System.out.println("Failed to register service: " + tag + " to discovery server");
		}
	}

}
