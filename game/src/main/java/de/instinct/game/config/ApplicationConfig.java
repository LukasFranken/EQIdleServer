package de.instinct.game.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class ApplicationConfig {

	@Value("${application.version:missing_version}")
	private String version;
	
	@Value("${server.port:missing_serverport}")
	private int port;
	
}