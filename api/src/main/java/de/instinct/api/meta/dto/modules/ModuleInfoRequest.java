package de.instinct.api.meta.dto.modules;

import java.util.List;

import de.instinct.api.core.modules.MenuModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleInfoRequest {

	private List<MenuModule> requestedModuleInfos;
	
}
