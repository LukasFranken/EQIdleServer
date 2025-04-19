package de.instinct.discovery.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import base.discovery.dto.RegistrationResponseCode;
import base.discovery.dto.ServiceInfoDTO;
import base.discovery.dto.ServiceRegistrationDTO;
import de.instinct.discovery.service.DiscoveryService;
import de.instinct.discovery.service.model.ServiceInfo;

@Service
public class DiscoveryServiceImpl implements DiscoveryService {
	
	private Map<String, ServiceInfo> services = new HashMap<>();

	@Override
	public String pingalive(String serviceTag) {
		if(services.containsKey(serviceTag)) {
			services.get(serviceTag).setLastAlivePing(System.currentTimeMillis());
			return "accepted";
		} else {
			return "denied";
		}
	}
	
	@Override
	public RegistrationResponseCode registerService(ServiceRegistrationDTO serviceRegistrationDTO) {
		RegistrationResponseCode response = services.containsKey(serviceRegistrationDTO.getServiceTag()) ? RegistrationResponseCode.OVERRIDDEN : RegistrationResponseCode.CREATED;
		services.put(serviceRegistrationDTO.getServiceTag(), ServiceInfo.builder()
				.name(serviceRegistrationDTO.getServiceName())
				.url(serviceRegistrationDTO.getServiceUrl())
				.version(serviceRegistrationDTO.getServiceVersion())
				.registrationTimestamp(System.currentTimeMillis())
				.lastAlivePing(System.currentTimeMillis())
				.build());
		return response;
	}

	@Override
	public ServiceInfoDTO discover(String serviceTag) {
		return getServiceInfoDTO(serviceTag);
	}

	@Override
	public List<ServiceInfoDTO> discoverAll() {
		List<ServiceInfoDTO> serviceInfoDTOs = new ArrayList<>();
		for (String tag : services.keySet()) {
			serviceInfoDTOs.add(getServiceInfoDTO(tag));
		}
		return serviceInfoDTOs;
	}
	
	private ServiceInfoDTO getServiceInfoDTO(String serviceTag) {
		if (services.containsKey(serviceTag)) {
			ServiceInfo serviceInfo = services.get(serviceTag);
			return ServiceInfoDTO.builder()
					.serviceTag(serviceTag)
					.serviceName(serviceInfo.getName())
					.serviceUrl(serviceInfo.getUrl())
					.serviceVersion(serviceInfo.getVersion())
					.lastAlivePingAgoMS(System.currentTimeMillis() - serviceInfo.getLastAlivePing())
					.build();
		} else {
			return null;
		}
	}

}