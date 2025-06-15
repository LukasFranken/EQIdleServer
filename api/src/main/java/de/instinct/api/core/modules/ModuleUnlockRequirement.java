package de.instinct.api.core.modules;

import de.instinct.api.meta.dto.PlayerRank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleUnlockRequirement {
	
	private PlayerRank requiredRank;
	private MenuModule module;

}
