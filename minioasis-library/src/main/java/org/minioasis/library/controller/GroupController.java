package org.minioasis.library.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.minioasis.library.domain.Group;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/admin/group")
public class GroupController {

	@Autowired
	private LibraryService service;
	
	@RequestMapping("/save")
	public String create(Model model) {
		model.addAttribute("group", new Group());
		return "group.form";
	}	
	
	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(@Valid Group group, BindingResult result, Model model) {

		if(result.hasErrors()){	
			return "group.form";			
		} else {		
			
			try{
				this.service.save(group);
			} 
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("code","error.not.unique");			
				return "group.form";				
			}
			
			model.addAttribute("group", new Group());
			model.addAttribute("done", group);
			
			return "group.form";
			
		}			
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(@RequestParam(value = "id", required = true) long id, Model model) {

		Group group = this.service.getGroup(id);
		
		if(group == null) {
			model.addAttribute("error", "ITEM NOT FOUND !");
			return "error";
		}
		
		model.addAttribute("group", group);
		return "group.form";
		
	}
	
	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	public String edit(@ModelAttribute("group") @Valid Group group, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "group.form";
		}
		else 
		{
			try{
				this.service.save(group);
			}
			catch (DataIntegrityViolationException eive)
			{
				result.rejectValue("code","error.not.unique");		
				return "group.form";
			}
			
			model.addAttribute("done", group);
			
			return "group.form";
			
		}
		
	}
	
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable("id") long id, Model model) {

		Group group = this.service.getGroup(id);
		if(group != null)
			this.service.deleteGroup(id);
		
		model.addAttribute("id", id);
		
		return "deleted";
		
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String groups(Model model, HttpServletRequest request, Pageable pageable) {

		Page<Group> page = this.service.findAllGroups(pageable);

		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		
		return "groups";
	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@RequestParam(value = "keyword", required = false) String keyword, HttpServletRequest request,
			Model model, Pageable pageable) {

		Page<Group> page = new PageImpl<Group>(new ArrayList<Group>(), pageable, 0);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		if(keyword != null){
			page = this.service.findByCodeOrNameContaining(keyword, pageable);
		}

		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		
		return "groups";

	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
}
