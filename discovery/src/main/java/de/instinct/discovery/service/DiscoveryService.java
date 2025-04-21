package de.instinct.discovery.service;

import java.util.List;

import de.instinct.api.discovery.dto.RegistrationResponseCode;
import de.instinct.api.discovery.dto.ServiceInfoDTO;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;

public interface DiscoveryService {

	String pingalive(String serviceTag);
	
	RegistrationResponseCode registerService(ServiceRegistrationDTO serviceRegistrationDTO);

	ServiceInfoDTO discover(String serviceTag);

	List<ServiceInfoDTO> discoverAll();

}
