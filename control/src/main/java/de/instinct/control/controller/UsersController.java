package de.instinct.control.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.instinct.control.service.base.BaseService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UsersController {
	
	private final BaseService baseService;
	
	@GetMapping("/users")
    public String home(Model model) {
		baseService.setModel(model);
		model.addAttribute("panel", "users");
		model.addAttribute("modal", "basemodal");
        return "home";
    }

}