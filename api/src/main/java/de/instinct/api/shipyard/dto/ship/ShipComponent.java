package de.instinct.api.shipyard.dto.ship;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "componentType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ShipCore.class, name = "core"),
    @JsonSubTypes.Type(value = ShipWeapon.class, name = "weapon"),
    @JsonSubTypes.Type(value = ShipEngine.class, name = "engine"),
    @JsonSubTypes.Type(value = ShipShield.class, name = "shield"),
    @JsonSubTypes.Type(value = ShipHull.class, name = "hull"),
})
public abstract class ShipComponent {
	
	private int id;

}
