package de.instinct.construction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.construction", 
		"de.instinct.eqspringutils",
		"de.instinct.base" })
public class ConstructionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConstructionApplication.class, args);
	}

}
