package org.minioasis.library.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.AttachmentState;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.AttachmentCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/attachment")
public class AttachmentListSearch {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("borrowablez")
	public YesNo[] populateBorrowable() {
		return YesNo.values() ;	
	}
	
	@ModelAttribute("statez")
	public AttachmentState[] populateAttachmentStates(){
		return AttachmentState.values();
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String attachments(Model model, Pageable pageable) {

		Page<Attachment> page = this.service.findAllAttachments(pageable);
		
		model.addAttribute("page", page);
		model.addAttribute("criteria",new AttachmentCriteria());
		
		return "attachments";
		
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") AttachmentCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {
		
		Page<Attachment> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagerType", "search");
		
		return "attachments";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {

		Locale locale = request.getLocale();
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, DecimalFormat.getInstance(locale), true));
		
	}
}
