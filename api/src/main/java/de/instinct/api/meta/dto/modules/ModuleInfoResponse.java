package de.instinct.api.meta.dto.modules;

import java.util.List;

import de.instinct.api.core.modules.ModuleUnlockRequirement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleInfoResponse {
	
	private List<ModuleUnlockRequirement> unlockRequirements;

}
