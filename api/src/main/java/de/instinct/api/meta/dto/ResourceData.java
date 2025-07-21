package de.instinct.api.meta.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceData {
	
	private List<ResourceAmount> resources;
	
	public boolean contains(ResourceAmount resource) {
		for (ResourceAmount res : resources) {
			if (res.getType() == resource.getType()) {
				if (res.getAmount() >= resource.getAmount()) {
					return true;
				}
			}
		}
		return false;
	}

}
