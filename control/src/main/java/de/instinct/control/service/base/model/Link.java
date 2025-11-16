package de.instinct.control.service.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Link {

	private String tag;
	private String url;
	private String label;

}
