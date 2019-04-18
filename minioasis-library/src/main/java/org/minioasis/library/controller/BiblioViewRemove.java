package org.minioasis.library.controller;

import java.util.List;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Item;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/biblio")
public class BiblioViewRemove {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, Model model) {
		model.addAttribute("biblio", this.service.getBiblio(id));
		return "biblio";

	}

	@RequestMapping(value = { "/{id}/items" }, method = RequestMethod.GET)
	public String viewItems(@PathVariable("id") long id, Model model) {
		List<Item> items = this.service.findAllItemsOrderByBarcode(id);
		model.addAttribute("items", items);
		return "biblio.items";

	}
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Biblio biblio = this.service.getBiblio(id);
		if(biblio != null)
			this.service.deleteBiblio(id);
		
		model.addAttribute("id", id);
		return "deleted";
		
	}
	
}
