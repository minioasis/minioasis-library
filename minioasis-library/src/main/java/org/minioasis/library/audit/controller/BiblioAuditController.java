package org.minioasis.library.audit.controller;

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
	
}

