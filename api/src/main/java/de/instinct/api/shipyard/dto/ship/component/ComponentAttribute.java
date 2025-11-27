package de.instinct.api.shipyard.dto.ship.component;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.instinct.api.core.annotation.Dto;
import de.instinct.api.shipyard.dto.ship.component.attribute.CoreAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.EngineAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.HullAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.ShieldAttribute;
import de.instinct.api.shipyard.dto.ship.component.attribute.WeaponAttribute;
import lombok.Data;

@Dto
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "componentAttributeType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CoreAttribute.class, name = "core"),
    @JsonSubTypes.Type(value = WeaponAttribute.class, name = "weapon"),
    @JsonSubTypes.Type(value = EngineAttribute.class, name = "engine"),
    @JsonSubTypes.Type(value = ShieldAttribute.class, name = "shield"),
    @JsonSubTypes.Type(value = HullAttribute.class, name = "hull"),
})
public abstract class ComponentAttribute {
	
	private int id;
	private double value;

}
