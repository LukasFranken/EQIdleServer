package de.instinct.control.service.base.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import de.instinct.api.control.model.Link;
import de.instinct.control.service.base.BaseService;

@Service
public class BaseServiceImpl implements BaseService {

	@Override
	public void setModel(Model model) {
		List<Link> links = Arrays.asList(
	            new Link("overview", "/overview", "Overview"),
	            new Link("users", "/users", "Users"),
	            new Link("shipyard", "/shipyard", "Shipyard"),
	            new Link("shop", "/shop", "Shop")
	        );
	    model.addAttribute("links", links);
	    model.addAttribute("modal", "basemodal");
	}

}
