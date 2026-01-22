package de.instinct.api.shipyard.dto.ship.component;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.meta.dto.ResourceAmount;
import de.instinct.api.shipyard.dto.ship.component.level.CoreLevel;
import de.instinct.api.shipyard.dto.ship.component.level.EngineLevel;
import de.instinct.api.shipyard.dto.ship.component.level.HullLevel;
import de.instinct.api.shipyard.dto.ship.component.level.ShieldLevel;
import de.instinct.api.shipyard.dto.ship.component.level.WeaponLevel;
import lombok.Data;

@Dto
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "componentLevelType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CoreLevel.class, name = "core"),
    @JsonSubTypes.Type(value = WeaponLevel.class, name = "weapon"),
    @JsonSubTypes.Type(value = EngineLevel.class, name = "engine"),
    @JsonSubTypes.Type(value = ShieldLevel.class, name = "shield"),
    @JsonSubTypes.Type(value = HullLevel.class, name = "hull"),
})
public abstract class ComponentLevel {
	
	private int level;
	private float requirementValue;
	private List<ComponentAttribute> attributes;
	private List<ResourceAmount> buildCost;

}
