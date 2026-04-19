package de.instinct.mining.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class ApplicationConfiguration {
	
	@Value("${application.version:missing_version}")
	private String version;
	
	@Value("${server.port:missing_serverport}")
	private int port;
	
	@Value("${gameserver.port.tcp:missing_tcpport}")
	private int gameTcpPort;
	
	@Value("${gameserver.port.udp:missing_udpport}")
	private int gameUdpPort;

}
