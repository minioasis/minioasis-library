package org.minioasis.library.controller;

import org.minioasis.library.domain.Patron;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/patron")
public class PatronViewRemove {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, Model model) {

		model.addAttribute("patron", this.service.getPatron(id));
		return "patron";

	}
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Patron patron = this.service.getPatron(id);
		if(patron != null)
			this.service.deletePatron(id);
		
		model.addAttribute("id", id);
		return "deleted";
		
	}
}
