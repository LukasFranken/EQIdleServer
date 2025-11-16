package de.instinct.control.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.instinct.api.core.API;
import de.instinct.api.core.config.APIConfiguration;
import de.instinct.control.service.base.BaseService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OverviewController {
	
	@Value(value = "${api.mode}")
	private APIConfiguration apiMode;
	
	private final BaseService baseService;

	@GetMapping("/overview")
    public String home(Model model) {
		baseService.setModel(model);
		model.addAttribute("panel", "overview");
		model.addAttribute("modal", "basemodal");
        return "home";
    }
	
	@GetMapping("/api")
    public String api(Model model) {
		if (!API.isInitialized()) {
			API.initialize(apiMode);
			if (apiMode == APIConfiguration.SERVER) {
				API.discovery().connect();
				API.shipyard().connect();
			}
		}
        return home(model);
    }
	
}
