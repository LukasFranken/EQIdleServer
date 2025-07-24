package de.instinct.discovery.service.impl;

import de.instinct.api.discovery.dto.ServiceInfoDTO;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;
import de.instinct.discovery.service.model.ServiceInfo;
import de.instinct.discovery.service.model.ServiceInfoMapper;

public class ServiceInfoMapperImpl implements ServiceInfoMapper {

	@Override
	public ServiceInfo map(ServiceRegistrationDTO serviceRegistrationDTO) {
		return ServiceInfo.builder()
				.protocol(serviceRegistrationDTO.getServiceProtocol())
				.address(serviceRegistrationDTO.getServiceAddress())
				.port(serviceRegistrationDTO.getServicePort())
				.endpoint(serviceRegistrationDTO.getServiceEndpoint())
				.version(serviceRegistrationDTO.getServiceVersion())
				.registrationTimestamp(System.currentTimeMillis())
				.lastAlivePing(System.currentTimeMillis())
				.build();
	}
	
	@Override
	public ServiceInfoDTO map(String serviceTag, ServiceInfo serviceInfo) {
		ServiceInfoDTO serviceInfoDTO = new ServiceInfoDTO();
		if (serviceInfo == null) {
			return serviceInfoDTO;
		}
		serviceInfoDTO.setServiceTag(serviceTag);
		serviceInfoDTO.setServiceProtocol(serviceInfo.getProtocol());
		serviceInfoDTO.setServiceAddress(serviceInfo.getAddress());
		serviceInfoDTO.setServicePort(serviceInfo.getPort());
		serviceInfoDTO.setServiceEndpoint(serviceInfo.getEndpoint());
		serviceInfoDTO.setServiceVersion(serviceInfo.getVersion());
		serviceInfoDTO.setLastAlivePingAgoMS(System.currentTimeMillis() - serviceInfo.getLastAlivePing());
		return serviceInfoDTO;
	}

}
