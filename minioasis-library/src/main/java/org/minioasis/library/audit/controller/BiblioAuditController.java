package org.minioasis.library.audit.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.audit.DeletedAuditEntity;
import org.minioasis.library.audit.service.AuditService;
import org.minioasis.library.domain.Biblio;
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
@RequestMapping("/admin/audit/biblio")
public class BiblioAuditController {

	@Autowired
	private AuditService service;
	
	@RequestMapping(value = "/{id}/list", method = RequestMethod.GET)
	public String biblios(@PathVariable("id") long id, Model model, Pageable pageable) {

		Page<Revision<Integer, Biblio>> page = this.service.findRevisions(id, pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("id", id);
		model.addAttribute("pagingType", "list");
		
		return "audit/biblios";
		
	}
	
	@RequestMapping(value = "/deleted.list", method = RequestMethod.GET)
	public String deletedBiblios(Model model, Pageable pageable) {

		Page<DeletedAuditEntity> page = this.service.listDeletedBibliosIn(null, 30, pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "list");
		
		return "audit/biblios.deleted";
		
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@RequestParam String title, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		Page<DeletedAuditEntity> page = this.service.listDeletedBibliosIn(title, 30, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "audit/biblios.deleted";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}

