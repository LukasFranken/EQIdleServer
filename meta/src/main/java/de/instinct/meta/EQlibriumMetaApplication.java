package de.instinct.meta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.meta", 
		"de.instinct.eqspringutils",
		"de.instinct.eqspringbase" })
public class EQlibriumMetaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EQlibriumMetaApplication.class, args);
	}

}
