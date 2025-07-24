package de.instinct.starmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.starmap", 
		"de.instinct.eqspringutils",
		"de.instinct.base" })
public class StarmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarmapApplication.class, args);
	}

}
