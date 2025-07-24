package de.instinct.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.auth", 
		"de.instinct.eqspringutils",
		"de.instinct.base" })
public class EQlibriumAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EQlibriumAuthenticationApplication.class, args);
	}

}
