package de.instinct.control.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

	@GetMapping("/")
    public RedirectView redirectToOverview() {
        return new RedirectView("/overview");
    }
	
    @GetMapping("/modal/{modalName}")
    public String getModal(@PathVariable String modalName, Model model) {
        return "content/modal/" + modalName + " :: " + modalName;
    }
    
}