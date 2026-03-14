package de.instinct.api.social.dto;

import java.util.List;

import de.instinct.api.core.annotation.Dto;
import lombok.Data;

@Dto
@Data
public class Group {
	
	private List<String> members;

}
