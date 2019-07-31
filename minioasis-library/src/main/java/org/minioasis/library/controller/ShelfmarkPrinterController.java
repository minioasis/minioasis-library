package org.minioasis.library.controller;

import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.search.Shelfmark;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("shelfmarks")
@RequestMapping("/admin/shelfmark")
public class ShelfmarkPrinterController {

	@Autowired
	private LibraryService service;	
	
	@RequestMapping(value = { "/list" }, method = RequestMethod.POST)
	public String search(@RequestParam String barcode , @ModelAttribute("shelfmarks") Set<Shelfmark> shelfmarks, Model model) {
		
		Item item = this.service.findByBarcode(barcode);

		if(item != null){
			shelfmarks.add(new Shelfmark(barcode, item.getShelfMark()));
		}
		
		model.addAttribute("shelfmarks", shelfmarks);
		
		return "shelfmarks";

	}
	
	@RequestMapping(value = { "/remove" }, method = RequestMethod.GET)
	public String remove(@RequestParam String barcode, @ModelAttribute("shelfmarks") Set<Shelfmark> shelfmarks, Model model) {
		
		Item item = this.service.findByBarcode(barcode);
		
		if(item != null){
			Shelfmark shelfmark = new Shelfmark(barcode,item.getShelfMark());
			shelfmarks.remove(shelfmark);
		}
		
		model.addAttribute("shelfmarks", shelfmarks);
		
		return "shelfmarks";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String shelfmarks(Model model) {

		model.addAttribute("shelfmarks", new HashSet<Shelfmark>());
		
		return "shelfmarks";
		
	}
}
