package de.instinct.api.discovery.service.impl;

import de.instinct.api.core.model.RESTRequest;
import de.instinct.api.core.model.SupportedRequestType;
import de.instinct.api.core.service.impl.BaseService;
import de.instinct.api.core.service.impl.ObjectJSONMapper;
import de.instinct.api.core.service.impl.URLBuilder;
import de.instinct.api.discovery.dto.RegistrationResponseCode;
import de.instinct.api.discovery.dto.ServiceInfoDTO;
import de.instinct.api.discovery.dto.ServiceRegistrationDTO;
import de.instinct.api.discovery.service.DiscoveryInterface;

public class Discovery extends BaseService implements DiscoveryInterface {
	
	public Discovery() {
		super("discovery");
		super.baseUrl = URLBuilder.build(ServiceInfoDTO.builder()
				.servicePort(6000)
				.serviceEndpoint("discovery")
				.build());
	}

	@Override
	public RegistrationResponseCode register(ServiceRegistrationDTO serviceRegistrationDTO) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.POST)
				.endpoint("register")
				.payload(serviceRegistrationDTO)
				.build());
		return ObjectJSONMapper.mapJSON(response, RegistrationResponseCode.class);
	}
	
	@Override
	public ServiceInfoDTO discover(String tag) {
		if (!isConnected()) return null;
		String response = super.sendRequest(RESTRequest.builder()
				.type(SupportedRequestType.GET)
				.endpoint("single")
				.pathVariable(tag)
				.build());
		return ObjectJSONMapper.mapJSON(response, ServiceInfoDTO.class);
	}

}
