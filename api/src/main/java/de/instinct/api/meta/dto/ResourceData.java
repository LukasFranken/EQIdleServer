package de.instinct.api.meta.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class ResourceData {
	
	private List<ResourceAmount> resources;
	
	public boolean contains(ResourceAmount resource) {
		for (ResourceAmount res : resources) {
			if (res.getType() == resource.getType()) {
				if (res.getAmount() >= Math.abs(resource.getAmount())) {
					return true;
				}
			}
		}
		return false;
	}

}
