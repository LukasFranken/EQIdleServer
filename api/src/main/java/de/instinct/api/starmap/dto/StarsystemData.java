package de.instinct.api.starmap.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.game.dto.MapPreview;
import de.instinct.api.meta.dto.ResourceAmount;
import lombok.Data;

@Dto
@Data
public class StarsystemData {
	
	private int id;
	private String name;
	private float mapPosX;
	private float mapPosY;
	private MapPreview mapPreview;
	private int ancientPoints;
	private int threatLevel;
	private long experience;
	private long duration;
	private List<ResourceAmount> resourceRewards;

}
