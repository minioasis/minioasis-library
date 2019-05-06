package org.minioasis.library.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.minioasis.library.domain.PatronType;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/patrontype")
public class PatronTypeController {

	@Resource
	private LibraryService service;

	@RequestMapping(value = { "/save" }, method = RequestMethod.GET)
	public String save(Model model) {
		model.addAttribute("patronType", new PatronType());
		return "patrontype.form";
	}	
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid PatronType patronType, BindingResult result, ModelMap model) {

		if(result.hasErrors()){		
			return "patrontype.form";	
		} else {
			
			try{
				this.service.save(patronType);
			}
			catch (DataIntegrityViolationException eive) 
			{
				result.rejectValue("code","error.not.unique");
				return "patrontype.form";				
			}
				
			return "redirect:/patrontype/" + patronType.getId();
			
		}			
	}
	
	@RequestMapping(value = { "/edit/{id}" }, method = RequestMethod.GET)
	public String edit(@PathVariable long id, Model model) {

		PatronType patronType = this.service.getPatronType(id);
		
		if(patronType == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}
		
		model.addAttribute("patronType", patronType);
		return "patrontype.form";
		
	}
	
	@RequestMapping(value = { "/edit/{id}" }, method = RequestMethod.POST)
	public String edit(@Valid PatronType patronType, BindingResult result) {
		
		if (result.hasErrors()) {
			return "patrontype.form";
		}
		else 
		{
			try{
				this.service.save(patronType);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("code","error.not.unique");
				
				return "patrontype.form";
			}
			
			return "redirect:/patrontype/" + patronType.getId();
			
		}
		
	}
	
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, Model model) {

		model.addAttribute("patronType", this.service.getPatronType(id));
		return "patrontype";

	}
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		PatronType patronType = this.service.getPatronType(id);
		if(patronType != null)
			this.service.delete(patronType);
		
		model.addAttribute("id", id);
		return "deleted";
		
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String patronTypes(Model model) {

		List<PatronType> patronTypes = this.service.findAllPatronTypes(Sort.by("name"));		
		model.addAttribute("patronTypes", patronTypes);
		return "patrontypes";
		
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {

		//Locale locale = request.getLocale();
		StringTrimmerEditor emptyTrimmer = new StringTrimmerEditor(true);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));		
		binder.registerCustomEditor(String.class, null, emptyTrimmer);
	}
	
}
