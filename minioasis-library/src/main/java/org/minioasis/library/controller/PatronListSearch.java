package org.minioasis.library.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/patron")
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
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String patrons(Model model, Pageable pageable) {

		Page<Patron> page = this.service.findAllPatrons(pageable);
		model.addAttribute("criteria", new PatronCriteria());
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "list");
		return "patrons";
		
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") PatronCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		Page<Patron> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "patrons";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
