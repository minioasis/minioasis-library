package org.minioasis.library.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Binding;
import org.minioasis.library.domain.Language;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.BiblioCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/biblio")
public class BiblioListSearch {

	@Autowired
	private LibraryService service;

	@ModelAttribute("langs")
	public Language[] populateLanguage() {
		return Language.values();	
	}
	
	@ModelAttribute("ats")
	public YesNo[] populateActives() {
		return YesNo.values();	
	}
	
	@ModelAttribute("binds")
	public Binding[] populateBindinds() {
		return Binding.values();	
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
		
		return "biblios";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String biblios(Model model, Pageable pageable) {

		Page<Biblio> page = this.service.findAllBiblios(pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("criteria", new BiblioCriteria());
		model.addAttribute("pagingType", "list");
		
		return "biblios";
		
	}

	@RequestMapping(value = { "/emptyitem/search" }, method = RequestMethod.GET)
	public String emptyItemSearch(@ModelAttribute("criteria") BiblioCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

/*		Page<Biblio> page = this.service.findAllBibliosByCriteriaDsl(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagerType", "search");*/
		
		return "biblios.null.item";

	}
	
	@RequestMapping(value = "/emptyitem/list", method = RequestMethod.GET)
	public String emptyItemBiblios(Model model, Pageable pageable) {

		Page<Biblio> page = this.service.findAllBibliosWithoutItem(pageable);		
		model.addAttribute("page", page);
		model.addAttribute("criteria", new BiblioCriteria());
		return "biblios.null.item";
		
	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
