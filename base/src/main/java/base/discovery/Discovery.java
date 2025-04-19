package base.discovery;

import base.discovery.dto.RegistrationResponseCode;
import base.discovery.dto.ServiceRegistrationDTO;

public interface Discovery {
	
	RegistrationResponseCode register(ServiceRegistrationDTO serviceRegistrationDTO);

}
