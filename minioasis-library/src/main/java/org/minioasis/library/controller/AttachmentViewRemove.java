package org.minioasis.library.controller;

import org.minioasis.library.domain.Attachment;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/attachment")
public class AttachmentViewRemove {

	@Autowired
	private LibraryService service;
	
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, Model model) {
		Attachment attachment = this.service.getAttachment(id);
		model.addAttribute("attachment", attachment);
		return "attachment";

	}
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Attachment attachment = this.service.getAttachment(id);
		if(attachment != null)
			this.service.deleteAttachment(id);
		
		model.addAttribute("id", id);
		return "deleted";
		
	}
	
}
