package de.instinct.matchmaking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.matchmaking", 
		"de.instinct.eqspringutils",
		"de.instinct.base" })
public class MatchmakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchmakerApplication.class, args);
	}

}
