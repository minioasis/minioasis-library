package org.minioasis.library.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.minioasis.library.domain.Publisher;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/publisher")
public class PublisherController {

	@Autowired
	private LibraryService service;

	@RequestMapping("/save")
	public String newPublisher(Model model) {
		model.addAttribute("publisher", new Publisher());
		return "library/publisher.form";
	}
	
	@RequestMapping("/save/{id}")
	public String newAndShowPublisher(@PathVariable long id, Model model) {
		
		Publisher done = service.getPublisher(id);
		
		model.addAttribute("publisher", new Publisher());
		model.addAttribute("done", done);
		return "library/publisher.form";
	}	
	
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid Publisher publisher , BindingResult result, ModelMap model) {

		if(result.hasErrors()){	
			return "publisher.form";			
		} else {		
			
			try{
				this.service.save(publisher);
			} 
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("name","error.not.unique");			
				return "publisher.form";				
			}
			
			return "redirect:/publisher/save/" + publisher.getId();
			
		}			
	}
	
	@RequestMapping(value = { "/edit/{id}" }, method = RequestMethod.GET)
	public String edit(@PathVariable("id") long id, Model model) {
		
		Publisher publisher = this.service.getPublisher(id);
		
		if(publisher == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}
		
		model.addAttribute("publisher", publisher);
		return "publisher.form";
		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@ModelAttribute("publisher") @Valid Publisher publisher, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "publisher.form";
		}
		else 
		{
			try{
				this.service.save(publisher);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("name","","not unique");		
				return "publisher.form";
			}
			
			return "redirect:/publisher/save/" + publisher.getId();
			
		}
		
	}

	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Publisher publisher = this.service.getPublisher(id);
		if(publisher != null)
			this.service.deletePublisher(id);
		
		model.addAttribute("id", id);
		return "deleted";
		
	}

	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@RequestParam(value = "keyword", required = false) String keyword, Model model, Pageable pageable) {

		Page<Publisher> page = new PageImpl<Publisher>(new ArrayList<Publisher>(), pageable, 0);
		
		if(keyword != null){
			page = this.service.findPublishersByNameContaining(keyword, pageable);
		}

		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pagingType", "search");
		
		return "publishers";

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String publishers(Model model, Pageable pageable) {

		Page<Publisher> page = this.service.findAllPublishers(pageable);
		model.addAttribute("page", page);
		return "publishers";
		
	}
	
	@RequestMapping(value = "/phase", method = RequestMethod.GET)
	public @ResponseBody List<Publisher> findPublishers(@RequestParam("phase") String phase) {

		if(phase != null && !phase.isEmpty()) {
			List<Publisher> publishers = service.findPublishersyNameContaining(phase);
			return publishers;
		}
	
		return null;
	}
	
}
