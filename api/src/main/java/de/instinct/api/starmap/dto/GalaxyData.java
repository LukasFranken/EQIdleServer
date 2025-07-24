package de.instinct.api.starmap.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class GalaxyData {
	
	private int id;
	private float mapPosX;
	private float mapPosY;
	private String name;
	private List<StarsystemData> starsystems;

}
