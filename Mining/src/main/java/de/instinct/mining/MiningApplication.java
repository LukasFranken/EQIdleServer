package de.instinct.mining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.mining", 
		"de.instinct.eqspringutils",
		"de.instinct.base" })
public class MiningApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiningApplication.class, args);
	}

}
