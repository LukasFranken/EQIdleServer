package de.instinct.discovery.service.model;

import de.instinct.api.discovery.dto.ServiceInfoDTO;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;

public interface ServiceInfoMapper {

	ServiceInfo map(ServiceRegistrationDTO serviceRegistrationDTO);
	
	ServiceInfoDTO map(String serviceTag, ServiceInfo serviceInfo);

}
