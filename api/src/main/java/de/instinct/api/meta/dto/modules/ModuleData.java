package de.instinct.api.meta.dto.modules;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.core.modules.MenuModule;
import lombok.Data;

@Dto
@Data
public class ModuleData {
	
	private List<MenuModule> enabledModules;

}
