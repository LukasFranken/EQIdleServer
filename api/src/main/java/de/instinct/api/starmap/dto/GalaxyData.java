package de.instinct.api.starmap.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalaxyData {
	
	private int id;
	private float mapPosX;
	private float mapPosY;
	private String name;
	private List<StarsystemData> starsystems;

}
