package org.minioasis.library.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.GroupEditor;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

@Controller
public class PatronController {

	@Autowired
	private LibraryService service;	
	
	@ModelAttribute("groups")
	public List<Group> populateGroups() {
		return this.service.findAllGroups(new Sort(Sort.Direction.ASC, "name"));	
	}
	
	@ModelAttribute("patronTypes")
	public List<PatronType> populatePatronTypes() {
		return this.service.findAllPatronTypes(new Sort(Sort.Direction.ASC, "name"));	
	}
	
	@RequestMapping(value = { "/patron.form" }, method = RequestMethod.GET)
	public String create(Model model) {

		model.addAttribute("patron", new Patron());
		return "patron.form";
			
	}
	
	@RequestMapping(value = { "/add.patron" }, method = RequestMethod.POST)
	public String create(@Valid Patron patron, BindingResult result) {

		if(result.hasErrors()){	
			return "library/patron.form";			
		} else {
			
			try{
				this.service.save(patron);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("cardKey","","cardkey or entangled not unique");	
				return "library/patron.form";
			}
			
			return "redirect:/library/patron/upload?id=" + patron.getId();
			
		}			
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true) long id, Model model) {

		Patron p = this.service.getPatron(id);
		
		if(p == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}
		
		model.addAttribute("patron", p);
		return "library/patron.form";
		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@ModelAttribute("patron") @Valid Patron patron, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "library/patron.form";
		}
		else 
		{
			try{
				this.service.edit(patron);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("cardKey","","cardkey or entangled not unique");				
				return "library/patron.form";
			}
			
			return "redirect:/library/patron/upload?id=" + patron.getId();
			
		}
		
	}
	
	@RequestMapping(value = {"/upload" }, method = RequestMethod.GET)
	public String uploadImage(@RequestParam(value = "id", required = true) long id, Model model) {

		Patron patron = this.service.getPatron(id);
		model.addAttribute("patron", patron);
		
		return "library/patron.image.upload.form";
	}
	
	@RequestMapping(value = { "/upload" }, method = RequestMethod.POST)
	public String uploadImage(@ModelAttribute("patron") Patron patron, @RequestParam(value = "id", required = true) long id, Model model) {

		patron.setId(id);
		this.service.upload(patron);

		return "redirect:/library/patron/" + patron.getId();
	}
	
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, Model model) {

		model.addAttribute("patron", this.service.getPatron(id));
		return "library/patron";

	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {

		//Locale locale = request.getLocale();
		StringTrimmerEditor emptyTrimmer = new StringTrimmerEditor(true);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));		
		binder.registerCustomEditor(String.class, null, emptyTrimmer);
		binder.registerCustomEditor(Group.class, new GroupEditor(service));
		binder.registerCustomEditor(byte[].class,new ByteArrayMultipartFileEditor());
/*		binder.registerCustomEditor(Set.class,"patronTypes", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   String id = (String) element;
	                   PatronType patronType = service.getPatronType(id);
	                   return patronType;
	               }
	               return null;
	           }

		});*/

	}
	
}
