package de.instinct.eq_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.eq_auth", 
		"de.instinct.eqspringutils",
		"de.instinct.eqspringbase" })
public class EQlibriumAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EQlibriumAuthenticationApplication.class, args);
	}

}
