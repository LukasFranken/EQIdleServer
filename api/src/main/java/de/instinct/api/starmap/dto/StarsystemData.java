package de.instinct.api.starmap.dto;

import java.util.List;

import de.instinct.api.meta.dto.ResourceAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StarsystemData {
	
	private int id;
	private String name;
	private float mapPosX;
	private float mapPosY;
	private int planets;
	private float ancientPoints;
	private int threatLevel;
	private long experience;
	private List<ResourceAmount> resourceRewards;

}
