package base.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;

@RequestMapping
public abstract class BaseServiceController extends BaseController {

	private String tag;
	private int port;
	private String version;

	public BaseServiceController(String tag, int port, String version) {
		this.tag = tag;
		this.port = port;
		this.version = version;
		connectToDiscovery();
	}

	@GetMapping("/connect")
	public ResponseEntity<String> connect() {
		connectToAPIs();
		return ResponseEntity.ok("OK");
	}

	private void connectToDiscovery() {
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
	
	protected abstract void connectToAPIs();

}
