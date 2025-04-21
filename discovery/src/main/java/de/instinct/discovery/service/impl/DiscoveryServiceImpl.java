package de.instinct.discovery.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import de.instinct.api.discovery.dto.RegistrationResponseCode;
import de.instinct.api.discovery.dto.ServiceInfoDTO;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;
import de.instinct.discovery.service.DiscoveryService;
import de.instinct.discovery.service.model.ServiceInfo;
import de.instinct.discovery.service.model.ServiceInfoMapper;

@Service
public class DiscoveryServiceImpl implements DiscoveryService {
	
	private Map<String, ServiceInfo> services;
	private ServiceInfoMapper mapper;
	
	public DiscoveryServiceImpl() {
		services = new HashMap<>();
		mapper = new ServiceInfoMapperImpl();
	}

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
		services.put(serviceRegistrationDTO.getServiceTag(), mapper.map(serviceRegistrationDTO));
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
			return mapper.map(serviceTag, serviceInfo);
		} else {
			return null;
		}
	}

}