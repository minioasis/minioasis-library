package org.minioasis.library.audit.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.audit.service.AuditService;
import org.minioasis.library.domain.Patron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/audit/patron")
public class PatronAuditController {

	@Autowired
	private AuditService service;
	
	@RequestMapping(value = "/{id}/list", method = RequestMethod.GET)
	public String patrons(@PathVariable("id") long id, Model model, Pageable pageable) {

		Page<Revision<Integer, Patron>> page = this.service.findPatronRevisions(id, pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("id", id);
		model.addAttribute("pagingType", "list");
		
		return "audit/patrons";
		
	}
	
	@RequestMapping(value = "/deleted.list", method = RequestMethod.GET)
	public String deletedPatrons(Model model, Pageable pageable) {

		Page<Object[]> page = this.service.listDeletedPatronsIn(null, 30, pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "list");
		
		return "audit/patrons.deleted";
		
	}
	
	@RequestMapping(value = { "/deleted.search" }, method = RequestMethod.GET)
	public String search(@RequestParam String keyword, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		Page<Object[]> page = this.service.listDeletedPatronsIn(keyword, 30, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "audit/patrons.deleted";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
}
