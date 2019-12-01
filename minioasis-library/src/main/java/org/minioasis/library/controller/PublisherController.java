package org.minioasis.library.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.minioasis.library.domain.Publisher;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/publisher")
public class PublisherController {

	@Autowired
	private LibraryService service;

	@RequestMapping("/save")
	public String create(Model model) {
		model.addAttribute("publisher", new Publisher());
		return "publisher.form";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid Publisher publisher, BindingResult result, ModelMap model) {

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
			
			model.addAttribute("publisher", new Publisher());
			model.addAttribute("done", publisher);
			
			return "publisher.form";
			
		}			
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true) long id, Model model) {
		
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
				result.rejectValue("name","error.not.unique");		
				return "publisher.form";
			}
			
			model.addAttribute("done", publisher);
			
			return "publisher.form";
			
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

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String publishers(Model model, HttpServletRequest request, Pageable pageable) {

		Page<Publisher> page = this.service.findAllPublishers(pageable);

		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		
		return "publishers";
		
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@RequestParam(value = "keyword", required = false) String keyword, HttpServletRequest request,
			Model model, Pageable pageable) {

		Page<Publisher> page = new PageImpl<Publisher>(new ArrayList<Publisher>(), pageable, 0);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		if(keyword != null){
			page = this.service.findPublishersByNameContaining(keyword, pageable);
		}

		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);

		return "publishers";

	}
	
	@RequestMapping(value = "/phase", method = RequestMethod.GET)
	public ResponseEntity<List<String>> findPublishers(@RequestParam("query") String name) {

		List<Publisher> publishers = service.findFirst10PublisherByNameContaining(name, Sort.by("name"));
		
		List<String> publisherNames = new ArrayList<String>();
		for(Publisher p : publishers) {
			publisherNames.add(p.getName());
		}
		
		return new ResponseEntity<List<String>>(publisherNames, HttpStatus.OK);

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
