package de.instinct.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.control", 
		"de.instinct.eqspringutils",
		"de.instinct.base" })
public class WebControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebControlApplication.class, args);
		if (!API.isInitialized()) {
			API.initialize(APIConfiguration.CLIENT);
			if (APIConfiguration.CLIENT == APIConfiguration.SERVER) {
				API.discovery().connect();
				API.shipyard().connect();
			}
		}
	}

}
