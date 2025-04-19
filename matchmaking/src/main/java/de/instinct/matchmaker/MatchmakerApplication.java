package de.instinct.matchmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.matchmaker", 
		"de.instinct.eqspringutils",
		"de.instinct.eqspringbase" })
public class MatchmakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchmakerApplication.class, args);
	}

}
