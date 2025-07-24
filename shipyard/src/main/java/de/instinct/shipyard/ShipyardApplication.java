package de.instinct.shipyard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.shipyard", 
		"de.instinct.eqspringutils",
		"de.instinct.base" })
public class ShipyardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipyardApplication.class, args);
	}

}
