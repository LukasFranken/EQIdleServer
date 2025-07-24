package de.instinct.api.starmap.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class SectorData {
	
	private List<GalaxyData> galaxies;

}
