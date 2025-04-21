package de.instinct.api.discovery.service.impl;

import de.instinct.api.core.service.BaseServiceInterface;
import de.instinct.api.discovery.dto.RegistrationResponseCode;
import de.instinct.api.discovery.dto.ServiceInfoDTO;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;

public interface DiscoveryInterface extends BaseServiceInterface {
	
	RegistrationResponseCode register(ServiceRegistrationDTO serviceRegistrationDTO);

	ServiceInfoDTO discover(String tag);
	

}
