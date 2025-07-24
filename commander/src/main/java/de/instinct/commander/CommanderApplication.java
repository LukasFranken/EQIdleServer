package de.instinct.commander;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.commander", 
		"de.instinct.eqspringutils",
		"de.instinct.base" })
public class CommanderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommanderApplication.class, args);
	}

}
