package org.minioasis.library.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.time.DateUtils;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.GroupEditor;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.PatronCriteria;
import org.minioasis.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@Controller
@RequestMapping("/patron/group.change")
public class PatronGroupChange {

	@Autowired
	private LibraryService service;
	
	@ModelAttribute("ats")
	public YesNo[] populateActives() {
		return YesNo.values();
	}
	
	@ModelAttribute("gps")
	public List<Group> populateGroups() {
		return this.service.findAllGroups();	
	}
	
	@ModelAttribute("pts")
	public List<PatronType> populatePatronTypes() {
		return this.service.findAllPatronTypes(Sort.by("name").ascending());	
	}
	
	@RequestMapping(value = { "/change" }, method = RequestMethod.POST)
	public String changeGroup(@ModelAttribute("criteria") @Valid PatronCriteria criteria , WebRequest webRequest, 
									BindingResult result , Model model , Pageable pageable) {
		
		Long groupId = criteria.getId();
		Long[] userIds = criteria.getIds();
		Date now = new Date();
		
		if(userIds.length == 0) {
			return "redirect:/patron/group.change/list?page=0&size=10&sort=cardKey,desc";
		}
		
		if(result.hasErrors()){
			return "patrons.group.change";	
			
		} else {
			
			if(userIds == null || (userIds != null && userIds.length == 0)){
				result.rejectValue("ids", "*", null, "patrons.not.found");
			}
			
			Group newGroup = this.service.getGroup(groupId);
			if(newGroup == null){
				result.rejectValue("id", "*", null, "group.required");
			}
			
			int ok = this.service.bulkUpdateGroup(Arrays.asList(userIds), newGroup, now);
			
			if(ok == 0){
				result.reject("failed", "failed");
				return "patrons.group.change";	
			}
			
		}

		String time = String.valueOf(now.getTime());
		
		return "redirect:/patron/group.change/" + groupId + "/" + time;
		
	}
	
	@RequestMapping(value = { "/{id}/{time}" }, method = RequestMethod.GET)
	public String view(@PathVariable("id") long id, @PathVariable("time") long time, Model model) {
		
		Group group = this.service.getGroup(id);

		Date updated = new Date(time);
		
		Date nearestSecond = DateUtils.round(updated, Calendar.SECOND);
		
		if(group != null){
			
			List<Patron> patrons = this.service.findByGroupAndUpdatedOrderByUpdatedDesc(group,updated);

			model.addAttribute("patrons", patrons);
			model.addAttribute("updated", nearestSecond);
			
		}
		
		return "patrons.group.change.result";	

	}
	
	@RequestMapping(value = { "/search" }, method = RequestMethod.GET)
	public String search(@ModelAttribute("criteria") PatronCriteria criteria, HttpServletRequest request, Map<String,String> params, 
			Model model, Pageable pageable) {

		Page<Patron> page = this.service.findByCriteria(criteria, pageable);
		
		String next = buildUri(request, page.getNumber() + 1);
		String previous = buildUri(request, page.getNumber() - 1);
		
		model.addAttribute("page", page);
		model.addAttribute("next", next);
		model.addAttribute("previous", previous);
		model.addAttribute("pagingType", "search");
		
		return "patrons.group.change";	

	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String patrons(Model model, Pageable pageable) {

		Page<Patron> page = this.service.findAllPatrons(pageable);
		
		model.addAttribute("criteria", new PatronCriteria());
		model.addAttribute("page", page);
		model.addAttribute("pagingType", "list");
		
		return "patrons.group.change";	
		
	}
	
	private String buildUri(HttpServletRequest request, int page){
		UriComponents uc = ServletUriComponentsBuilder.fromRequest(request)
		        .replaceQueryParam("page", "{id}").build()
		        .expand(page);
		
		return uc.toUriString();
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		
		binder.registerCustomEditor(Group.class, new GroupEditor(service));
		binder.registerCustomEditor(byte[].class,new ByteArrayMultipartFileEditor());

	}
	
}
