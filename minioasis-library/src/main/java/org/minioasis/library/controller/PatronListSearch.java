package org.minioasis.library.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.PatronCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/patron")
public class PatronListSearch {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("ats")
	public YesNo[] populateActives() {
		return YesNo.values();
	}
	
	@ModelAttribute("gps")
	public List<Group> populateGroups() {
		return this.service.findAllGroups();	
	}
	
	@ModelAttribute("pts")
	public List<PatronType> populatePatronTypes() {
		return this.service.findAllPatronTypes(Sort.by("name").ascending());	
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String patrons(Model model, HttpServletRequest request, Pageable pageable) {

		Page<Patron> page = this.service.findAllPatrons(pageable);

		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("criteria", new PatronCriteria());
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("page", page);
		
		return "patrons";
		
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") PatronCriteria criteria, HttpServletRequest request, 
			Model model, Pageable pageable) {

		Page<Patron> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);

		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("page", page);
		
		return "patrons";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
