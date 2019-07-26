package org.minioasis.library.controller;

import java.util.List;

import javax.validation.Valid;

import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.GroupEditor;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

@Controller
@RequestMapping("/admin/patron")
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
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("patron", new Patron());
		return "patron.form";		
	}
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String create(@Valid Patron patron, BindingResult result) {

		if(result.hasErrors()){
			return "patron.form";			
		} else {
			
			try{
				this.service.save(patron);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("cardKey","","cardkey or entangled not unique");	
				return "patron.form";
			}
			
			return "redirect:/admin/patron/" + patron.getId();
			
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
		return "patron.form";
		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@ModelAttribute("patron") @Valid Patron patron, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "patron.form";
		}
		else 
		{
			try{
				this.service.edit(patron);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("cardKey","","cardkey or entangled not unique");				
				return "patron.form";
			}
			
			return "redirect:/admin/patron/" + patron.getId();
			
		}
		
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		
		binder.registerCustomEditor(Group.class, new GroupEditor(service));
		binder.registerCustomEditor(byte[].class,new ByteArrayMultipartFileEditor());

	}
	
}
