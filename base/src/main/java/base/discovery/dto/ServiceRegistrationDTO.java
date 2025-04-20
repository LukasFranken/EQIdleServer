package base.discovery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegistrationDTO {
	
	private String serviceTag;
    private String serviceUrl;
    private String serviceVersion;

}
