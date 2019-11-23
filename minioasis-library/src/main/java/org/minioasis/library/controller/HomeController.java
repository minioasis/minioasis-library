package org.minioasis.library.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@GetMapping("/")
    public String home() {
        return "pub/index";
    }
	
	@GetMapping("/admin")
    public String adminHome(Model model) {
		model.addAttribute("now", LocalDateTime.now());
        return "admin";
    }
	
	@GetMapping("/admin/config")
    public String config(Model model) {
        return "config";
    }
	
	@GetMapping("/admin/reports")
    public String report(Model model) {
        return "reports";
    }

	@GetMapping("/admin/audit")
    public String audit(Model model) {
        return "audit";
    }
	
    @RequestMapping("/admin/opac")
    String opac(Model model){
        return "opac";
    }
	
}
