package org.minioasis.library.audit.controller;

import org.minioasis.library.audit.AuditRevisionEntity;
import org.minioasis.library.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/audit/revision")
public class RevisionEntityController {

	@Autowired
	private AuditService service;
	
	@RequestMapping(value = "/{rev}", method = RequestMethod.GET)
	public String getRevisionEntity(@PathVariable("rev") int rev, Model model) {
		
		AuditRevisionEntity re = service.getRevisionEntity(rev);
		model.addAttribute("revEntity", re);
		
		return "audit/revision.entity";
	}
}
