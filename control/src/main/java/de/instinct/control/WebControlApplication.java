package de.instinct.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { 
		"de.instinct.control", 
		"de.instinct.eqspringutils",
		"de.instinct.base" })
public class WebControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebControlApplication.class, args);
	}

}
