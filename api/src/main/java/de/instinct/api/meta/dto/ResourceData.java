package de.instinct.api.meta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceData {
	
	private long credits;
	private long iron;
	private long gold;
	private long quartz;
	private long deuterium;
	private long equilibrium;

}
