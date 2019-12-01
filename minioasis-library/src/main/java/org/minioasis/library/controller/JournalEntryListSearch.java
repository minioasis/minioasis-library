package org.minioasis.library.controller;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.JournalEntry;
import org.minioasis.library.domain.search.JournalEntryCriteria;
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
@RequestMapping("/admin/journalentry")
public class JournalEntryListSearch {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") JournalEntryCriteria criteria, HttpServletRequest request, 
			Model model, Pageable pageable) {

		Page<JournalEntry> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		
		return "journalentries";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String journalEntries(Model model, HttpServletRequest request, Pageable pageable) {

		Page<JournalEntry> page = this.service.findAllJournalEntries(pageable);		
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("criteria", new JournalEntryCriteria());
		
		return "journalentries";
		
	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
}
