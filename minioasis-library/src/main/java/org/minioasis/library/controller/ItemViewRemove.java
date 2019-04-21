package org.minioasis.library.controller;

import org.minioasis.library.domain.Item;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/item")
public class ItemViewRemove {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, Model model) {
		
		Item item = this.service.getItem(id);
		model.addAttribute("item", item);
		
		return "item";

	}
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Item item = this.service.getItem(id);
		if(item != null)
			this.service.deleteItem(id);
		
		model.addAttribute("id", id);
		return "deleted";
		
	}
	
}
