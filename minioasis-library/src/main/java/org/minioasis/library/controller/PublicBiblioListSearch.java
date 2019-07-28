package org.minioasis.library.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.search.BiblioCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/biblio")
public class PublicBiblioListSearch {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, Model model) {
		
		model.addAttribute("biblio", this.service.getBiblio(id));
		
		return "pub/biblio";

	}

	@RequestMapping(value = { "/{id}/items" }, method = RequestMethod.GET)
	public String viewItems(@PathVariable("id") long id, Model model) {
		
		List<Item> items = this.service.findAllItemsOrderByBarcode(id);
		model.addAttribute("items", items);
		
		return "pub/biblio.items";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String home(Model model, Pageable pageable) {

		Page<Biblio> page = this.service.findAllBiblios(pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("criteria", new BiblioCriteria());
		model.addAttribute("pagingType", "list");
		
		return "pub/biblios";
		
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") BiblioCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		Page<Biblio> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "pub/biblios";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
