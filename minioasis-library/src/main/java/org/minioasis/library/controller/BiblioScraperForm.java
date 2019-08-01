package org.minioasis.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/biblio")
public class BiblioScraperForm {

	@RequestMapping(value = { "/isbn.form" }, method = RequestMethod.GET)
	public String form(Model model) {
		return "biblio.isbn.form";
	}
	
	@RequestMapping(value = { "/scraper" }, method = RequestMethod.GET)
	public String checkout(@RequestParam String isbn, Model model) {

		if(isbn == null || isbn.equals("")) {
			return "biblio.isbn.form";
		}else {
			model.addAttribute("isbn", isbn);
		}
		
		return "biblio.isbn.form";
	}
}
