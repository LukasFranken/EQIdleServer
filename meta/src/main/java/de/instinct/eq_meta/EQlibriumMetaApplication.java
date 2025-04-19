package de.instinct.eq_meta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.game", 
		"de.instinct.eqspringutils",
		"de.instinct.eqspringbase" })
public class EQlibriumMetaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EQlibriumMetaApplication.class, args);
	}

}
