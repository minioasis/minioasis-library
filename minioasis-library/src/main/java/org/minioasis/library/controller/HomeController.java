package org.minioasis.library.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
    public String getHomePage(Model model) {
		model.addAttribute("now", new Date());
        return "index";
    }
	
}
