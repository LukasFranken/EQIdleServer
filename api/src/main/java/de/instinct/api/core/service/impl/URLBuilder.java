package de.instinct.api.core.service.impl;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.api.discovery.dto.ServiceInfoDTO;

public class URLBuilder {
	
	public static String build(ServiceInfoDTO serviceInfo) {
		String url = "";
		if (API.configuration == APIConfiguration.CLIENT) {
			url += serviceInfo.getServiceProtocol() + "://" + serviceInfo.getServiceAddress() + ":";
		} else {
			url += "http://localhost:";
		}
		url += serviceInfo.getServicePort() + "/" + serviceInfo.getServiceEndpoint();
		return url;
	}

}
