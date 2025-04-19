package de.instinct.discovery.service;

import java.util.List;

import base.discovery.dto.RegistrationResponseCode;
import base.discovery.dto.ServiceInfoDTO;
import base.discovery.dto.ServiceRegistrationDTO;

public interface DiscoveryService {

	String pingalive(String serviceTag);
	
	RegistrationResponseCode registerService(ServiceRegistrationDTO serviceRegistrationDTO);

	ServiceInfoDTO discover(String serviceTag);

	List<ServiceInfoDTO> discoverAll();

}
