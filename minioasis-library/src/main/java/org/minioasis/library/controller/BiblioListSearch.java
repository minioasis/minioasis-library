package org.minioasis.library.controller;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.BiblioType;
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
@RequestMapping("/admin/biblio")
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

	@ModelAttribute("bts")
	public BiblioType[] populateBiblioTypes() {
		return BiblioType.values();	
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") BiblioCriteria criteria, HttpServletRequest request, 
			Model model, Pageable pageable) {

		Page<Biblio> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		
		return "biblios";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String biblios(Model model, HttpServletRequest request, Pageable pageable) {

		Page<Biblio> page = this.service.findAllBiblios(pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("criteria", new BiblioCriteria());
		
		return "biblios";
		
	}

	@RequestMapping(value = { "/search.uncomplete" }, method = RequestMethod.GET)
	public String umcompleteSearch(@ModelAttribute("criteria") BiblioCriteria criteria, HttpServletRequest request,
			Model model, Pageable pageable) {

		Page<Biblio> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		
		return "biblios.ghost";

	}
	
	@RequestMapping(value = "/list.uncomplete", method = RequestMethod.GET)
	public String umcompleteBiblios(Model model, HttpServletRequest request, Pageable pageable) {

		Page<Biblio> page = this.service.findAllUncompleteBiblios(pageable);

		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("criteria", new BiblioCriteria());
		
		return "biblios.ghost";
		
	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
