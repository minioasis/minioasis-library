package org.minioasis.library.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@GetMapping("/")
    public String home(Model model) {
		model.addAttribute("now", new Date());
        return "index";
    }
	
	@GetMapping("/config")
    public String config(Model model) {
		model.addAttribute("now", new Date());
        return "config";
    }
	
	@GetMapping("/reports")
    public String report(Model model) {
		model.addAttribute("now", new Date());
        return "reports";
    }
	
    @RequestMapping("/opac")
    String opac(Model model){
        return "opac";
    }
	
}
