package de.instinct.meta.service.model;

import de.instinct.api.meta.dto.LoadoutData;
import de.instinct.api.meta.dto.ModuleData;
import de.instinct.api.meta.dto.ProfileData;
import de.instinct.api.meta.dto.ResourceData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserData {
	
	private ProfileData profile;
	private ModuleData modules;
	private ResourceData resources;
	private LoadoutData loadout;

}
