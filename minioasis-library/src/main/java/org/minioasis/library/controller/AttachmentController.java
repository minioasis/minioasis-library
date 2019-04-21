package org.minioasis.library.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.validation.Valid;

import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.AttachmentState;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/attachment")
public class AttachmentController {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("borrowables")
	public YesNo[] populateBorrowable() {
		return YesNo.values() ;	
	}

	@ModelAttribute("states")
	public AttachmentState[] populateStates() {
		return AttachmentState.values();	
	}
	
	@RequestMapping(value = { "/save/{iid}" }, method = RequestMethod.GET)
	public String save(@PathVariable("iid") long iid , Model model) {

		Attachment attachment = new Attachment();
		Item item = this.service.getItem(iid);
		if(item != null){
			attachment.setItem(item);	
		}

		model.addAttribute("attachment", attachment );
		return "attachment.form";

	}
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String add(@ModelAttribute("attachment") @Valid Attachment attachment, BindingResult result) {

		String barcode = attachment.getBarcode();
		
		if(result.hasErrors()){
			
			return "attachment.form";
			
		} else {
			
			Item item = this.service.findByBarcode(barcode);
			if(item != null){
				result.rejectValue("barcode","","not unique");			
				return "attachment.form";
			}

			attachment.setState(AttachmentState.IN_LIBRARY);
			attachment.setLastCheckin(attachment.getFirstCheckin());
			
			try{
				
				this.service.save(attachment);
				
			}catch (DataIntegrityViolationException eive){
				
				result.rejectValue("barcode","","not unique");			
				return "attachment.form";
				
			}			
			
		}
		
		return "redirect:/attachment/"+ attachment.getId();
			
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true) long id, Model model) {

		Attachment attachment = this.service.getAttachment(id);

		model.addAttribute("attachment", attachment);
		return "attachment.form";

	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@ModelAttribute("attachment") @Valid Attachment attachment, BindingResult result, SessionStatus status) {

		String barcode = attachment.getBarcode();
		
		if(result.hasErrors()){
			
			return "attachment.form";
			
		} else {

			Item item = this.service.findByBarcode(barcode);
			if(item != null){
				result.rejectValue("barcode","","not unique");			
				return "attachment.form";
			}
						
			try{
				
				this.service.save(attachment);
				status.setComplete();
				
			}catch (DataIntegrityViolationException eive){
				
				result.rejectValue("barcode","","not unique");			
				return "attachment.form";
				
			}
			
		}

		return "redirect:/attachment/"+ attachment.getId();
			
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {

		Locale locale = request.getLocale();
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, DecimalFormat.getInstance(locale), true));

	}
	
}
