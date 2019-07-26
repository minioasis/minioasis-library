package org.minioasis.library.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.AccountType;
import org.minioasis.library.domain.JournalEntryLine;
import org.minioasis.library.domain.search.JournalEntryLineCriteria;
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
@RequestMapping("/admin/journalentry.line")
public class JournalEntryLineListSearch {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("accountTypez")
	public AccountType[] populateAccountTypes() {
		return AccountType.values();	
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") JournalEntryLineCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		Page<JournalEntryLine> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "journalentry.lines";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String biblios(Model model, Pageable pageable) {

		Page<JournalEntryLine> page = this.service.findAllJournalEntryLines(pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("criteria", new JournalEntryLineCriteria());
		model.addAttribute("pagingType", "list");
		
		return "journalentry.lines";
		
	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
}
